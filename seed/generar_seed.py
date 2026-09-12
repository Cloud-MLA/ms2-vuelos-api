"""
Generador de datos ficticios para MS2 (PostgreSQL).
SEED fijo para reproducibilidad. Uso: python generar_seed.py
Requiere: pip install faker psycopg2-binary
"""

import random
from datetime import datetime, timedelta, timezone
import psycopg2
from faker import Faker

SEED = 42
random.seed(SEED)
fake = Faker("es_ES")
Faker.seed(SEED)

# --- Conexión (ajusta según tu entorno) ---
DB_HOST = "localhost"
DB_PORT = 5434  # o 5432 si corres el script dentro del contenedor/VM
DB_NAME = "vuelos_db"
DB_USER = "ms2_user"
DB_PASSWORD = "ms2_pass"

ALIANZAS = ["Star Alliance", "SkyTeam", "Oneworld"]
CLASES = ["A", "B", "C", "D", "E", "F"]
TIPOS_VUELO = ["Nacional", "Internacional"]
ESTADOS_VUELO = ["Programado", "Embarcando", "Despegado", "Aterrizado", "Retrasado", "Cancelado"]
AREAS_OPERATIVAS = ["Rampa", "Equipajes", "Seguridad", "Mantenimiento", "Plataforma"]

AEROPUERTOS_NACIONALES = ["LIM", "CUZ", "AQP", "PIU", "TRU", "IQT", "TCQ"]
AEROPUERTOS_INTERNACIONALES = ["BOG", "MIA", "MAD", "SCL", "GRU", "MEX", "JFK"]

N_AEROLINEAS = 5
N_AERONAVES = 15
ASIENTOS_POR_AERONAVE = 30
N_EMPLEADOS_TRIPULACION = 20
N_VUELOS = 2000


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
    # Orden inverso a las FKs, para no romper integridad referencial
    tablas = [
        "opera_tripulacion", "tripulacion", "operativo_tierra", "empleado",
        "asiento", "vuelo", "aeronave", "aerolinea"
    ]
    for t in tablas:
        cur.execute(f"DELETE FROM {t}")


def crear_aerolineas(cur):
    rucs = []
    for _ in range(N_AEROLINEAS):
        ruc = generar_ruc()
        nombre = fake.company()[:100]
        alianza = random.choice(ALIANZAS)
        cur.execute(
            "INSERT INTO aerolinea (ruc, nombre, alianza) VALUES (%s, %s, %s)",
            (ruc, nombre, alianza)
        )
        rucs.append(ruc)
    return rucs


def crear_aeronaves(cur):
    placas = []
    for i in range(N_AERONAVES):
        placa = generar_placa(i)
        modelo = random.choice(["A320", "A321", "B737-800", "B787", "A319"])
        fabricante = "Airbus" if "A3" in modelo else "Boeing"
        capacidad = random.choice([150, 160, 180, 200, 220])
        clase = random.choice(CLASES)
        cur.execute(
            "INSERT INTO aeronave (placa, modelo, fabricante, capacidad, clase) VALUES (%s, %s, %s, %s, %s)",
            (placa, modelo, fabricante, capacidad, clase)
        )
        placas.append(placa)
    return placas


def crear_asientos(cur, placas):
    asiento_id = 1
    for placa in placas:
        for fila in range(1, (ASIENTOS_POR_AERONAVE // 6) + 1):
            for letra in "ABCDEF":
                codigo = f"{fila}{letra}"
                cur.execute(
                    "INSERT INTO asiento (id, aeronave_placa, codigo) VALUES (%s, %s, %s)",
                    (asiento_id, placa, codigo)
                )
                asiento_id += 1


def crear_tripulacion(cur):
    empleado_ids = []
    for i in range(1, N_EMPLEADOS_TRIPULACION + 1):
        nombre = fake.first_name()
        apellido = fake.last_name()
        fecha_nac = fake.date_of_birth(minimum_age=25, maximum_age=60)
        cur.execute(
            "INSERT INTO empleado (id, nombre, apellido, fecha_nacimiento) VALUES (%s, %s, %s, %s)",
            (i, nombre, apellido, fecha_nac)
        )
        num_licencia = f"LIC-{1000 + i}"
        cur.execute(
            "INSERT INTO tripulacion (empleado_id, num_licencia) VALUES (%s, %s)",
            (i, num_licencia)
        )
        empleado_ids.append(i)
    return empleado_ids


def crear_vuelos_y_asignaciones(cur, rucs, placas, tripulantes):
    fecha_base = datetime(2026, 9, 15, tzinfo=timezone.utc)
    for vuelo_id in range(1, N_VUELOS + 1):
        tipo = random.choice(TIPOS_VUELO)
        if tipo == "Nacional":
            origen, destino = random.sample(AEROPUERTOS_NACIONALES, 2)
        else:
            origen = "LIM"
            destino = random.choice(AEROPUERTOS_INTERNACIONALES)

        numero = f"{random.choice(['LA', 'H2', 'AV', 'JA'])}{random.randint(1000, 9999)}"
        hora_programada = fecha_base + timedelta(
            days=random.randint(0, 30), hours=random.randint(0, 23), minutes=random.choice([0, 15, 30, 45])
        )
        estado = random.choices(
            ESTADOS_VUELO, weights=[50, 10, 10, 15, 10, 5]
        )[0]
        hora_real = hora_programada + timedelta(minutes=random.randint(-10, 60)) \
            if estado in ("Despegado", "Aterrizado") else None

        aerolinea_ruc = random.choice(rucs)
        aeronave_placa = random.choice(placas)

        cur.execute(
            """INSERT INTO vuelo
               (id, numero, origen, destino, hora_programada, hora_real, estado, tipo, aerolinea_ruc, aeronave_placa)
               VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)""",
            (vuelo_id, numero, origen, destino, hora_programada, hora_real, estado, tipo, aerolinea_ruc, aeronave_placa)
        )

        # Asignar 2-4 tripulantes al azar a este vuelo
        asignados = random.sample(tripulantes, random.randint(2, 4))
        for empleado_id in asignados:
            cur.execute(
                "INSERT INTO opera_tripulacion (vuelo_id, tripulacion_empleado_id) VALUES (%s, %s)",
                (vuelo_id, empleado_id)
            )


def main():
    conn = conectar()
    conn.autocommit = False
    cur = conn.cursor()
    try:
        print("Limpiando tablas...")
        limpiar_tablas(cur)

        print("Creando aerolíneas...")
        rucs = crear_aerolineas(cur)

        print("Creando aeronaves...")
        placas = crear_aeronaves(cur)

        print("Creando asientos...")
        crear_asientos(cur, placas)

        print("Creando empleados y tripulación...")
        tripulantes = crear_tripulacion(cur)

        print(f"Creando {N_VUELOS} vuelos y asignaciones de tripulación...")
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
