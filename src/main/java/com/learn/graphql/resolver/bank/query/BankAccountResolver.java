package com.learn.graphql.resolver.bank.query;

import com.learn.graphql.context.dataloader.DataLoaderRegistryFactory;
import com.learn.graphql.domain.bank.Asset;
import com.learn.graphql.domain.bank.BankAccount;
import com.learn.graphql.domain.bank.Client;
import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankAccountResolver implements GraphQLResolver<BankAccount> {

    /**
     * Executor definido en com.learn.graphql.config.AsyncExecutorConfig
     */
    private final Executor bankAccountExecutor;

    /**
     * Resolver asincrono para los assets
     *
     * @param bankAccount
     * @return CompletableFuture<List < Asset>> Permite ejecutar varios resolvers de manera asincrona
     */
    public CompletableFuture<List<Asset>> assets(BankAccount bankAccount) {
        return CompletableFuture.supplyAsync(
                () -> {
                    log.info("Getting assets for bank account id {}", bankAccount.getId());
                    return List.of();
                }, bankAccountExecutor);
    }

    @PreAuthorize("hasAuthority('get:bank_account_balance')")
    public CompletableFuture<BigDecimal> balance(BankAccount bankAccount, DataFetchingEnvironment environment) {
        DataLoader<UUID, BigDecimal> dataLoader = environment.getDataLoader(DataLoaderRegistryFactory.BALANCE_DATA_LOADER);
        return dataLoader.load(bankAccount.getId(), bankAccount);
    }

    public Client client(BankAccount bankAccount) {
        log.info("Requesting client data for bank account id {}", bankAccount.getId());

        return Client.builder()
                .id(UUID.randomUUID())
                .firstName("Philip")
                .lastName("Starritt").build();
    }

    public CompletableFuture<Client> clientAsync(BankAccount bankAccount) {
        log.info("Requesting client data for bank account id {}", bankAccount.getId());
        return CompletableFuture.supplyAsync(
                () -> Client.builder()
                        .id(UUID.randomUUID())
                        .firstName("Philip")
                        .lastName("Starritt").build());
    }

    public DataFetcherResult<Client> client2(BankAccount bankAccount) {
        log.info("Requesting client data for bank account id {}", bankAccount.getId());
        boolean anyError = false;
        DataFetcherResult<Client> result = null;
        // Call service to gather data from deveral sources
        if (anyError) {
            result = DataFetcherResult.<Client>newResult()
                    .data(Client.builder().id(UUID.randomUUID()).firstName("Philip").lastName("Starritt").build())
                    .error(new GenericGraphQLError("Some error during data fetch from server XXX"))
                    .build();
        } else {
            result = DataFetcherResult.<Client>newResult()
                    .data(Client.builder().id(UUID.randomUUID()).firstName("Philip").lastName("Starritt").build())
                    .build();
        }
        return result;
    }


}
