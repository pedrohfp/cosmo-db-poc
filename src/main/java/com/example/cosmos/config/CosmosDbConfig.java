package com.example.cosmos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuração do Azure Cosmos DB baseada em properties.
 * As credenciais devem ser fornecidas via variáveis de ambiente.
 */
@ConfigurationProperties(prefix = "azure.cosmos")
public class CosmosDbConfig {

    private static final Logger log = LoggerFactory.getLogger(CosmosDbConfig.class);

    public final String uri;
    public final String key;
    public final String database;

    public final String eventsContainerName;
    public final String eventsContainerPartition;
    public final Integer eventsContainerTTL;

    public CosmosDbConfig(
        String uri,
        String key,
        String database,
        @DefaultValue("events")
        String eventsContainerName,
        @DefaultValue("/transactionId")
        String eventsContainerPartition,
        @DefaultValue("-1")
        Integer eventsContainerTTL
    ) {
        this.uri = uri;
        this.key = key;
        this.database = database;
        this.eventsContainerName = eventsContainerName;
        this.eventsContainerPartition = eventsContainerPartition;
        this.eventsContainerTTL = eventsContainerTTL;

        log.debug("=== CosmosDbConfig Initialized ===");
        log.info("uri={}", uri);
        log.info("key={}", maskKey(key));
        log.info("database={}", database);
        log.info("eventsContainerName={}", eventsContainerName);
        log.info("eventsContainerPartition={}", eventsContainerPartition);
        log.info("eventsContainerTTL={}", eventsContainerTTL);
    }

    /**
     * Mascara a chave do Cosmos DB para exibição segura em logs.
     * Mostra apenas os primeiros 4 caracteres.
     */
    private String maskKey(String key) {
        if (key == null || key.length() <= 4) {
            return "****";
        }
        return key.substring(0, 4) + "*".repeat(Math.min(key.length() - 4, 20));
    }
}

