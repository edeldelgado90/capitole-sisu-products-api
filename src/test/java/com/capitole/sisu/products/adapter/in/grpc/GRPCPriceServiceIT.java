package com.capitole.sisu.products.adapter.in.grpc;

import com.google.protobuf.Timestamp;
import io.grpc.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.capitole.sisu.products.adapter.in.grpc.proto.PriceServiceGrpc;
import com.capitole.sisu.products.adapter.in.grpc.proto.GetCurrentPriceByProductAndBrandRequest;
import com.capitole.sisu.products.adapter.in.grpc.proto.PriceResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class GRPCPriceServiceIT {

    @Autowired
    private GRPCPriceService priceService;
    private Server server;
    private ManagedChannel channel;
    private PriceServiceGrpc.PriceServiceBlockingStub blockingStub;

    @BeforeEach
    public void setUp() throws IOException {
        server = ServerBuilder.forPort(0)
                .addService(priceService)
                .build()
                .start();

        channel = ManagedChannelBuilder.forAddress("localhost", server.getPort())
                .usePlaintext()
                .build();
        blockingStub = PriceServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        if (channel != null) {
            channel.shutdown();
        }
        if (server != null) {
            server.shutdown();
            server.awaitTermination();
        }
    }

    @Test
    @DisplayName("Get current price by product and brand must return a valid price")
    public void getCurrentPriceByProductAndBrandMustReturnValidPrice() {
        long productId = 35455;
        long brandId = 1;
        String testDate = "2020-07-01T12:00:00";

        GetCurrentPriceByProductAndBrandRequest request =
                GetCurrentPriceByProductAndBrandRequest.newBuilder()
                        .setProductId(productId)
                        .setBrandId(brandId)
                        .setDate(
                                Timestamp.newBuilder()
                                        .setSeconds(
                                                LocalDateTime.parse(testDate)
                                                        .atZone(ZoneId.systemDefault())
                                                        .toEpochSecond())
                                        .setNanos(LocalDateTime.parse(testDate).getNano())
                                        .build())
                        .build();

        PriceResponse response = blockingStub.getCurrentPriceByProductAndBrand(request);

        assertThat(response).isNotNull();
        assertThat(response.getBrandId()).isEqualTo(1);
        assertThat(response.getStartDate().getSeconds()).isEqualTo(1592229600);
        assertThat(response.getEndDate().getSeconds()).isEqualTo(1609455599);
        assertThat(response.getPriceList()).isEqualTo(4);
        assertThat(response.getProductId()).isEqualTo(35455);
        assertThat(response.getPrice()).isEqualByComparingTo(38.95);
    }

    @Test
    @DisplayName("Get current price by invalid product must return an error")
    public void getCurrentPriceByInvalidProductMustReturnAnError() {
        long productId = 123;
        long brandId = 1;
        String testDate = "2020-07-01T12:00:00";

        GetCurrentPriceByProductAndBrandRequest request =
                GetCurrentPriceByProductAndBrandRequest.newBuilder()
                        .setProductId(productId)
                        .setBrandId(brandId)
                        .setDate(
                                Timestamp.newBuilder()
                                        .setSeconds(
                                                LocalDateTime.parse(testDate)
                                                        .atZone(ZoneId.systemDefault())
                                                        .toEpochSecond())
                                        .setNanos(LocalDateTime.parse(testDate).getNano())
                                        .build())
                        .build();

        assertThatThrownBy(() -> blockingStub.getCurrentPriceByProductAndBrand(request))
                .isInstanceOf(StatusRuntimeException.class)
                .hasMessageContaining("NOT_FOUND: No price found for the given product and brand.");
    }
}
