<!--
SPDX-FileCopyrightText: the secureCodeBox authors

SPDX-License-Identifier: Apache-2.0
-->

# DefectDojo Client Java

Java Client to interact with the DefectDojo API.

## Dependency Information

You can find the latest version on [Maven Central](https://central.sonatype.com/artifact/io.securecodebox/defectdojo-client/).

### Maven

```xml
<dependency>
    <groupId>io.securecodebox</groupId>
    <artifactId>defectdojo-client</artifactId>
    <version>1.0.0.-beta2</version>
</dependency>
```

### Gradle

```groovy
implementation group: 'io.securecodebox', name: 'defectdojo-client', version: '1.0.0.-beta2'
```

## Development

To run a local build clone this repo and just invoke the following command in the repo directory: 

```shell
mvn clean install
```

## Supported DefectDojo Versions

The client is supposed to be compatible with DefectDojo 1.10 and later, older version of DefectDojo might still work, but are not officially supported.

## Example

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.config.Config;
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
