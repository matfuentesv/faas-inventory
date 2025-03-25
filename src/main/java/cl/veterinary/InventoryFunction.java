package cl.veterinary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import cl.veterinary.model.Inventory;
import cl.veterinary.service.InventoryService;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;


public class InventoryFunction {

    private static final ApplicationContext context =
            new SpringApplicationBuilder(SpringBootAzureApp.class).run();

    private final InventoryService inventoryService =
            context.getBean(InventoryService.class);



    @FunctionName("saveInsumo")
    public HttpResponseMessage saveInsumo(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "inventario") HttpRequestMessage<Optional<Inventory>> request,
            final ExecutionContext context) {

        Inventory dto = request.getBody().orElseThrow();
        Inventory saved = inventoryService.save(dto);
        return request.createResponseBuilder(HttpStatus.CREATED).body(saved).build();
    }

    @FunctionName("uploadInventarioCSV")
    public HttpResponseMessage uploadCSV(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "inventario/upload", dataType = "binary") HttpRequestMessage<Optional<byte[]>> request,
            final ExecutionContext context) {

        byte[] content = request.getBody().orElseThrow();
        try (InputStream is = new ByteArrayInputStream(content)) {
            List<Inventory> cargados = inventoryService.loadfromCSV(is);
            return request.createResponseBuilder(HttpStatus.OK).body(cargados).build();
        } catch (IOException e) {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al leer archivo: " + e.getMessage()).build();
        }
    }

    @FunctionName("stockCritico")
    public HttpResponseMessage stockCritico(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "inventario/critico/{minimo}") HttpRequestMessage<Optional<String>> request,
            @BindingName("minimo") int minimo,
            final ExecutionContext context) {

        List<Inventory> criticos = inventoryService.findStockCritico(minimo);
        return request.createResponseBuilder(HttpStatus.OK).body(criticos).build();
    }
}
