package com.example.cosmos.config;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.IndexingMode;
import com.azure.cosmos.models.IndexingPolicy;
import com.azure.cosmos.models.ThroughputProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * Azure Cosmos DB Bean Factory.
 * <p>
 * Cria e configura os beans necessários para interagir com o Azure Cosmos DB.
 * </p>
 */
@Configuration(proxyBeanMethods = false)
public class CosmosDbBeans {

    private static final Logger log = LoggerFactory.getLogger(CosmosDbBeans.class);
    private final CosmosDbConfig config;

    // Define o throughput máximo para autoscale do database (e.g., 4000 RU/s)
    private static final int AUTOSCALE_MAX_THROUGHPUT = 4000;

    /**
     * Construtor de injeção de dependência.
     */
    public CosmosDbBeans(CosmosDbConfig cosmosDbConfig) {
        this.config = Objects.requireNonNull(cosmosDbConfig, "cosmosDbConfig cannot be null");
        log.info("CosmosDbBeans initialized with config for database: {}", config.database);
    }

    /**
     * Bean do CosmosDatabase.
     * Cria o database se não existir com throughput autoscale.
     */
    @Bean("cosmosDb")
    public CosmosDatabase cosmosDb() {
        log.debug("Creating cosmosDb...");
        final CosmosDatabase database;
        try (final CosmosClient client = getCosmosClient()) {
            final var throughput = ThroughputProperties.createAutoscaledThroughput(AUTOSCALE_MAX_THROUGHPUT);
            client.createDatabaseIfNotExists(config.database, throughput);
            database = client.getDatabase(config.database);
        }
        log.info("CosmosDb created: {}", config.database);
        return database;
    }

    /**
     * Bean do container "events".
     * Cria o container se não existir com as propriedades configuradas.
     */
    @Bean("events")
    public CosmosContainer events(CosmosDatabase cosmosDb) {
        log.debug("Creating events container...");
        final var properties = containerDefaults(
            config.eventsContainerName,
            config.eventsContainerPartition,
            config.eventsContainerTTL
        );
        cosmosDb.createContainerIfNotExists(properties);

        final var container = cosmosDb.getContainer(config.eventsContainerName);
        log.info("Events container created: {}", config.eventsContainerName);
        return container;
    }

    /**
     * Cria as propriedades padrão para um container Cosmos DB.
     *
     * @param containerName    nome do container
     * @param partitionKeyPath caminho da partition key (ex: /transactionId)
     * @param ttl             time-to-live em segundos (-1 = nunca expira, null = desabilitado)
     * @return propriedades configuradas do container
     */
    private CosmosContainerProperties containerDefaults(
        String containerName,
        String partitionKeyPath,
        Integer ttl
    ) {
        final var properties = new CosmosContainerProperties(
            containerName,
            partitionKeyPath
        );

        /*
         * Não cria índices secundários. O IndexingMode.NONE previne a criação de índices secundários
         * em outras propriedades nos documentos, que seriam usados para queries eficientes nesses campos.
         * Não desabilita a funcionalidade central relacionada à primary key.
         */
        final var indexingPolicy = new IndexingPolicy();
        indexingPolicy.setIndexingMode(IndexingMode.NONE);
        properties.setIndexingPolicy(indexingPolicy);

        /*
         * Define o Time-to-Live padrão para itens neste container:
         * - Se ttl for null, TTL será desabilitado para o container
         * - Se ttl for -1, itens nunca expiram por padrão
         * - Se ttl for um inteiro positivo, itens expiram após esse número de segundos
         */
        properties.setDefaultTimeToLiveInSeconds(ttl);

        return properties;
    }

    /**
     * Cria um CosmosClient otimizado para performance:
     * <ul>
     *   <li><b>Gateway mode</b> - mais resiliente a problemas de rede transitórios</li>
     *   <li><b>ConsistencyLevel EVENTUAL</b> - melhor balance de performance, disponibilidade e custo</li>
     *   <li><b>contentResponseOnWrite desabilitado</b> - melhora performance de escrita reduzindo overhead de rede e consumo de RU</li>
     * </ul>
     *
     * @return cliente Cosmos configurado
     * @see <a href="https://www.ibm.com/think/topics/cap-theorem">What is the CAP theorem?</a>
     */
    private CosmosClient getCosmosClient() {
        return new CosmosClientBuilder()
            .endpoint(config.uri)
            .key(config.key)
            // Gateway mode é mais resiliente a problemas de rede transitórios
            .gatewayMode()
            .contentResponseOnWriteEnabled(false)
            .consistencyLevel(ConsistencyLevel.EVENTUAL)
            .buildClient();
    }
}

