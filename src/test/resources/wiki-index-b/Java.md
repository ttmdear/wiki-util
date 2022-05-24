[//]: "java"

# Others

[//]: "lvl1"

## Set up controllers

[//]: "lvl2"

```java
@PostMapping("/create-new-version/{mccpaId}/{vrsnId}")
public MccpaDetailEntity createNewVersion(@PathVariable Long mccpaId, @PathVariable Long vrsnId) {
    mccpaDetailService.createVersion(mccpaId);
    return mccpaDetailViewService.getMccpaDetail(new MccpaDetailEntityId(mccpaId, vrsnId));
}

```

### Spring application
[//]: "lvl3"

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

#### Non indexed
[//]: "lvl4"

**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specime
# Others 2
[//]: "lvl1b"
## Set up controllers
```java
@PostMapping("/create-new-version/{mccpaId}/{vrsnId}")
public MccpaDetailEntity createNewVersion(@PathVariable Long mccpaId, @PathVariable Long vrsnId) {
    mccpaDetailService.createVersion(mccpaId);
    return mccpaDetailViewService.getMccpaDetail(new MccpaDetailEntityId(mccpaId, vrsnId));
}
```
### Spring application

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
#### Non indexed
[//]: "lvl4"

**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specime