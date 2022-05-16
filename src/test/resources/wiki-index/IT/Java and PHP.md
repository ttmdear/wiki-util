[//]: ''java-and-php"

# Others

[//]: "others"

## Set up controllers

[//]: "set-up-controllers"

```java
@PostMapping("/create-new-version/{mccpaId}/{vrsnId}")
public MccpaDetailEntity createNewVersion(@PathVariable Long mccpaId, @PathVariable Long vrsnId) {
    mccpaDetailService.createVersion(mccpaId);
    return mccpaDetailViewService.getMccpaDetail(new MccpaDetailEntityId(mccpaId, vrsnId));
}

```

## Spring application
[//]: "spring-application"

```java
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
```

## Non indexed

**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specime
