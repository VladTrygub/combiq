package ru.atott.combiq.data.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import ru.atott.combiq.service.bean.UserQualifier;
import ru.atott.combiq.service.bean.UserType;
import ru.atott.combiq.service.user.UserService;

@Component
public class SecurityCommands implements CommandMarker {

    @Autowired
    private UserService userService;

    @CliCommand(value = "grant role")
    public String grant(
            @CliOption(key = "userType", mandatory = true) UserType userType,
            @CliOption(key = "login", mandatory = true) String login,
            @CliOption(key = "role", mandatory = true) String role) {

        userService.grantRole(new UserQualifier(userType, login), role);

        return "Done";
    }

    @CliCommand(value = "revoke role")
    public String revoke(
            @CliOption(key = "userType", mandatory = true) UserType userType,
            @CliOption(key = "login", mandatory = true) String login,
            @CliOption(key = "role", mandatory = true) String role) {

        userService.revokeRole(new UserQualifier(userType, login), role);

        return "Done";
    }
}
