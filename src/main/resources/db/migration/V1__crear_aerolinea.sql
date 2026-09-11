CREATE TABLE aerolinea (
                           ruc VARCHAR(11) PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           alianza VARCHAR(20) NOT NULL
                               CHECK (alianza IN ('Star Alliance', 'SkyTeam', 'Oneworld'))
);
