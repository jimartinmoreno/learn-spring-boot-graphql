package com.learn.graphql.context.dataloader;

import com.learn.graphql.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
public class DataLoaderRegistryFactory {

    public static final String BALANCE_DATA_LOADER = "BALANCE_DATA_LOADER";

    private final BalanceService balanceService;
    private final Executor balanceExecutor;

    public DataLoaderRegistry create(String userId) {
        var registry = new DataLoaderRegistry();
        registry.register(BALANCE_DATA_LOADER, createBalanceDataLoader(userId));
        return registry;
    }

    //    private DataLoader<UUID, BigDecimal> createBalanceDataLoader(String userId) {
    //        return DataLoader.newMappedDataLoader((bankAccountIds, environment) ->
    //                CompletableFuture.supplyAsync(() ->
    //                                balanceService.getBalanceFor((Map) environment.getKeyContexts(), userId),
    //                        balanceExecutor));
    //    }


    private DataLoader<UUID, BigDecimal> createBalanceDataLoader(String userId) {
        return DataLoaderFactory.newMappedDataLoader((bankAccountIds, env) ->
                CompletableFuture.supplyAsync(() -> {
                            System.out.println("bankAccountIds = " + bankAccountIds);
                            System.out.println("getKeyContexts = " + env.getKeyContexts());
                            System.out.println("getKeyContextsList = " + env.getKeyContextsList());
                            return balanceService.getBalanceFor((Map) env.getKeyContexts(), userId);
                        },
                        balanceExecutor));
    }
}
