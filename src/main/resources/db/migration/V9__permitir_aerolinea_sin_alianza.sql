-- El check original de V1 no contemplaba aerolineas sin alianza global.
-- La carga de seeds (docs/contratos/enums.md) incluye 'Ninguna' para ~1/3
-- de las aerolineas, reflejando que no todas pertenecen a una alianza.
ALTER TABLE aerolinea DROP CONSTRAINT aerolinea_alianza_check;
ALTER TABLE aerolinea ADD CONSTRAINT aerolinea_alianza_check
    CHECK (alianza IN ('Star Alliance', 'SkyTeam', 'Oneworld', 'Ninguna'));
