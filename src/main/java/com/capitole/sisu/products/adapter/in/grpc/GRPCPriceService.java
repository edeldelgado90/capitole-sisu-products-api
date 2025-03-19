package com.capitole.sisu.products.adapter.in.grpc;

import com.capitole.sisu.products.adapter.in.grpc.proto.GetCurrentPriceByProductAndBrandRequest;
import com.capitole.sisu.products.adapter.in.grpc.proto.PriceResponse;
import com.capitole.sisu.products.adapter.in.grpc.proto.PriceServiceGrpc;
import com.capitole.sisu.products.domain.price.CurrentPrice;
import com.capitole.sisu.products.domain.price.PriceNotFoundException;
import com.capitole.sisu.products.port.in.rest.RestPriceInPort;
import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@GrpcService
public class GRPCPriceService extends PriceServiceGrpc.PriceServiceImplBase {
    private final RestPriceInPort restPriceInPort;

    public GRPCPriceService(RestPriceInPort restPriceInPort) {
        this.restPriceInPort = restPriceInPort;
    }

    @Override
    public void getCurrentPriceByProductAndBrand(GetCurrentPriceByProductAndBrandRequest request, StreamObserver<PriceResponse> responseObserver) {
        Timestamp timestamp = request.getDate();
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
        LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        Mono<CurrentPrice> currentPrice = restPriceInPort.getCurrentPrice(request.getProductId(), request.getBrandId(), date);

        currentPrice.map(p -> {
            PriceResponse response = PriceResponseMapper.toResponse(p);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return response;
        }).onErrorResume(PriceNotFoundException.class, ex -> {
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription(ex.getMessage())));
            return Mono.empty();
        }).subscribe();
    }
}
