package insper.store.account;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@Tag(name = "Account", description = "Account API")
public class AccountResource implements AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts/info")
    @Tag(name = "Info", description = "Account API Info")
    public ResponseEntity<Map<String, String>> info() {
        return new ResponseEntity<Map<String, String>>(
                Map.ofEntries(
                        Map.entry("microservice.name", AccountApplication.class.getSimpleName()),
                        Map.entry("os.arch", System.getProperty("os.arch")),
                        Map.entry("os.name", System.getProperty("os.name")),
                        Map.entry("os.version", System.getProperty("os.version")),
                        Map.entry("file.separator", System.getProperty("file.separator")),
                        Map.entry("java.class.path", System.getProperty("java.class.path")),
                        Map.entry("java.home", System.getProperty("java.home")),
                        Map.entry("java.vendor", System.getProperty("java.vendor")),
                        Map.entry("java.vendor.url", System.getProperty("java.vendor.url")),
                        Map.entry("java.version", System.getProperty("java.version")),
                        Map.entry("line.separator", System.getProperty("line.separator")),
                        Map.entry("path.separator", System.getProperty("path.separator")),
                        Map.entry("user.dir", System.getProperty("user.dir")),
                        Map.entry("user.home", System.getProperty("user.home")),
                        Map.entry("user.name", System.getProperty("user.name")),
                        Map.entry("jar", new java.io.File(
                                AccountApplication.class.getProtectionDomain()
                                        .getCodeSource()
                                        .getLocation()
                                        .getPath())
                                .toString())),
                HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Create Account", description = "Create Account")
    public ResponseEntity<AccountOut> create(AccountIn in) {
        // parser
        Account account = AccountParser.to(in);
        // insert using service
        account = accountService.create(account);
        // return
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(account.id())
                        .toUri())
                .body(AccountParser.to(account));
    }

    @Override
    @Operation(summary = "Update", description = "Update Account")
    public ResponseEntity<AccountOut> update(String id, AccountIn in) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    @Operation(summary = "LogIn", description = "LogIn Account")
    public ResponseEntity<AccountOut> login(LoginIn in) {
        Account account = accountService.login(in.email(), in.password());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(AccountParser.to(account));
    }


    @Override
    @Operation(summary = "Read", description = "Read Account")
    public ResponseEntity<AccountOut> read(String idUser) {

        Account account = accountService.read(idUser);

        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        AccountOut accountOut = AccountParser.to(account);
        
        return ResponseEntity.ok(accountOut);
    }

    // @Override
    // @Operation(summary = "Fallback", description = "Fallback Account")
    // public ResponseEntity<AccountOut> fallbackGetAccounts(Throwable t) {
    //     return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    // }

}
