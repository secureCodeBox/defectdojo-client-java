# DefectDojo Client Java

Java Client to interact with the DefectDojo API.

## Supported DefectDojo Versions

The client is supposed to be compatible with DefectDojo 1.10 and later, older version of DefectDojo might still work, but are not officially supported.

## Example

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.service.ProductTypeService;

import java.net.URISyntaxException;

public class DefectDojoClientTest {
    public static void main(String[] args) throws URISyntaxException, JsonProcessingException {

        // Configure DefectDojo URl and APIv2 Key
        var conf = new DefectDojoConfig("https://defectdojo.example.com", "f8....");

        var productTypeService = new ProductTypeService(conf);
        var productTypes = productTypeService.search();

        System.out.println("ProductTypes:");
        for (var productType : productTypes) {
            System.out.println(" - Id: " + productType.getId() + ", Name: '" + productType.getName()) + "'";
        }
    }
}
```

## Testing a modified client
To build a gradle project that depends on this client one can use `./gradlew build --include-build <path_to_this_folder>`. Gradle will then build the project with this local version of the the client.