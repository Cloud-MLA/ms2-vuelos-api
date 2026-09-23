"""
Generador de datos ficticios para MS2 (PostgreSQL) — version 20k.
Corre dentro de un contenedor Docker; las variables de conexion
se inyectan via docker-compose (env_file: .env), no se lee ningun
archivo .env manualmente aqui.

SEED fijo para reproducibilidad.
"""

import os
import random
from datetime import datetime, timedelta, timezone
import psycopg2
from psycopg2.extras import execute_values
from faker import Faker

SEED = 42
random.seed(SEED)
fake = Faker("es_ES")
Faker.seed(SEED)

# --- Conexion (desde variables de entorno) ---
DB_HOST = os.environ["DB_HOST"]
DB_PORT = int(os.environ["DB_PORT"])
DB_NAME = os.environ["DB_NAME"]
DB_USER = os.environ["DB_USER"]
DB_PASSWORD = os.environ["DB_PASSWORD"]

ALIANZAS = ["Star Alliance", "SkyTeam", "Oneworld"]
CLASES = ["A", "B", "C", "D", "E", "F"]
TIPOS_VUELO = ["Nacional", "Internacional"]
ESTADOS_VUELO = ["Programado", "Embarcando", "Despegado", "Aterrizado", "Retrasado", "Cancelado"]
AREAS_OPERATIVAS = ["Rampa", "Equipajes", "Seguridad", "Mantenimiento", "Plataforma"]

AEROPUERTOS_NACIONALES = ["LIM", "CUZ", "AQP", "PIU", "TRU", "IQT", "TCQ"]
AEROPUERTOS_INTERNACIONALES = ["BOG", "MIA", "MAD", "SCL", "GRU", "MEX", "JFK"]

N_AEROLINEAS = 5
N_AERONAVES = 100
ASIENTOS_POR_AERONAVE = 210  # 100 x 210 = 21,000 >= 20,000
N_EMPLEADOS_TRIPULACION = 300
N_VUELOS = 20500  # margen sobre el minimo de 20,000

BATCH_SIZE = 2000  # tamano de lote para inserciones masivas


def generar_ruc():
    return "20" + "".join(str(random.randint(0, 9)) for _ in range(9))


def generar_placa(i):
    return f"OB-{1000 + i}"


def conectar():
    return psycopg2.connect(
        host=DB_HOST, port=DB_PORT, dbname=DB_NAME,
        user=DB_USER, password=DB_PASSWORD
    )


def limpiar_tablas(cur):
    tablas = [
        "opera_tripulacion", "tripulacion", "operativo_tierra", "empleado",
        "asiento", "vuelo", "aeronave", "aerolinea"
    ]
    for t in tablas:
        cur.execute(f"DELETE FROM {t}")


def crear_aerolineas(cur):
    rucs = []
    valores = []
    for _ in range(N_AEROLINEAS):
        ruc = generar_ruc()
        nombre = fake.company()[:100]
        alianza = random.choice(ALIANZAS)
        valores.append((ruc, nombre, alianza))
        rucs.append(ruc)
    execute_values(
        cur,
        "INSERT INTO aerolinea (ruc, nombre, alianza) VALUES %s",
        valores
    )
    return rucs


def crear_aeronaves(cur):
    placas = []
    valores = []
    for i in range(N_AERONAVES):
        placa = generar_placa(i)
        modelo = random.choice(["A320", "A321", "B737-800", "B787", "A319"])
        fabricante = "Airbus" if "A3" in modelo else "Boeing"
        capacidad = ASIENTOS_POR_AERONAVE
        clase = random.choice(CLASES)
        valores.append((placa, modelo, fabricante, capacidad, clase))
        placas.append(placa)
    execute_values(
        cur,
        "INSERT INTO aeronave (placa, modelo, fabricante, capacidad, clase) VALUES %s",
        valores
    )
    return placas


def crear_asientos(cur, placas):
    asiento_id = 1
    valores = []
    for placa in placas:
        for fila in range(1, (ASIENTOS_POR_AERONAVE // 6) + 1):
            for letra in "ABCDEF":
                codigo = f"{fila}{letra}"
                valores.append((asiento_id, placa, codigo))
                asiento_id += 1
                if len(valores) >= BATCH_SIZE:
                    execute_values(
                        cur,
                        "INSERT INTO asiento (id, aeronave_placa, codigo) VALUES %s",
                        valores
                    )
                    valores = []
    if valores:
        execute_values(
            cur,
            "INSERT INTO asiento (id, aeronave_placa, codigo) VALUES %s",
            valores
        )
    return asiento_id - 1  # total de asientos creados


def crear_tripulacion(cur):
    empleado_ids = []
    valores_empleado = []
    valores_tripulacion = []
    for i in range(1, N_EMPLEADOS_TRIPULACION + 1):
        nombre = fake.first_name()
        apellido = fake.last_name()
        fecha_nac = fake.date_of_birth(minimum_age=25, maximum_age=60)
        valores_empleado.append((i, nombre, apellido, fecha_nac))
        valores_tripulacion.append((i, f"LIC-{1000 + i}"))
        empleado_ids.append(i)

    execute_values(
        cur,
        "INSERT INTO empleado (id, nombre, apellido, fecha_nacimiento) VALUES %s",
        valores_empleado
    )
    execute_values(
        cur,
        "INSERT INTO tripulacion (empleado_id, num_licencia) VALUES %s",
        valores_tripulacion
    )
    return empleado_ids


def crear_vuelos_y_asignaciones(cur, rucs, placas, tripulantes):
    fecha_base = datetime(2026, 9, 15, tzinfo=timezone.utc)

    valores_vuelo = []
    valores_opera = []

    def flush():
        if valores_vuelo:
            execute_values(
                cur,
                """INSERT INTO vuelo
                   (id, numero, origen, destino, hora_programada, hora_real, estado, tipo, aerolinea_ruc, aeronave_placa)
                   VALUES %s""",
                valores_vuelo
            )
            valores_vuelo.clear()
        if valores_opera:
            execute_values(
                cur,
                "INSERT INTO opera_tripulacion (vuelo_id, tripulacion_empleado_id) VALUES %s",
                valores_opera
            )
            valores_opera.clear()

    for vuelo_id in range(1, N_VUELOS + 1):
        tipo = random.choice(TIPOS_VUELO)
        if tipo == "Nacional":
            origen, destino = random.sample(AEROPUERTOS_NACIONALES, 2)
        else:
            origen = "LIM"
            destino = random.choice(AEROPUERTOS_INTERNACIONALES)

        numero = f"{random.choice(['LA', 'H2', 'AV', 'JA'])}{random.randint(1000, 9999)}"
        hora_programada = fecha_base + timedelta(
            days=random.randint(0, 60), hours=random.randint(0, 23), minutes=random.choice([0, 15, 30, 45])
        )
        estado = random.choices(
            ESTADOS_VUELO, weights=[50, 10, 10, 15, 10, 5]
        )[0]
        hora_real = hora_programada + timedelta(minutes=random.randint(-10, 60)) \
            if estado in ("Despegado", "Aterrizado") else None

        aerolinea_ruc = random.choice(rucs)
        aeronave_placa = random.choice(placas)

        valores_vuelo.append((
            vuelo_id, numero, origen, destino, hora_programada, hora_real,
            estado, tipo, aerolinea_ruc, aeronave_placa
        ))

        asignados = random.sample(tripulantes, random.randint(2, 4))
        for empleado_id in asignados:
            valores_opera.append((vuelo_id, empleado_id))

        # Flush sincronizado: siempre vuelo antes que opera_tripulacion,
        # y ambos del mismo lote de vuelos juntos.
        if len(valores_vuelo) >= BATCH_SIZE:
            flush()

    flush()  # remanente final


def main():
    conn = conectar()
    conn.autocommit = False
    cur = conn.cursor()
    try:
        print("Limpiando tablas...")
        limpiar_tablas(cur)

        print(f"Creando {N_AEROLINEAS} aerolineas...")
        rucs = crear_aerolineas(cur)

        print(f"Creando {N_AERONAVES} aeronaves...")
        placas = crear_aeronaves(cur)

        print(f"Creando asientos ({N_AERONAVES} x {ASIENTOS_POR_AERONAVE})...")
        total_asientos = crear_asientos(cur, placas)
        print(f"  Total asientos: {total_asientos}")

        print(f"Creando {N_EMPLEADOS_TRIPULACION} empleados/tripulacion...")
        tripulantes = crear_tripulacion(cur)

        print(f"Creando {N_VUELOS} vuelos y asignaciones de tripulacion...")
        crear_vuelos_y_asignaciones(cur, rucs, placas, tripulantes)

        conn.commit()
        print("Seed completado exitosamente.")
    except Exception as e:
        conn.rollback()
        print(f"Error, se hizo rollback: {e}")
        raise
    finally:
        cur.close()
        conn.close()


if __name__ == "__main__":
    main()
