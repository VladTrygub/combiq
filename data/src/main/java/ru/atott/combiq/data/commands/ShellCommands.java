package ru.atott.combiq.data.commands;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.ExitShellRequest;
import org.springframework.shell.core.JLineShellComponent;
import org.springframework.shell.core.SimpleParser;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class ShellCommands implements CommandMarker, ApplicationContextAware {

    private ApplicationContext ctx;

    @CliCommand(value = "help", help = "List all commands usage")
    public void obtainHelp(
            @CliOption(key = { "", "command" }, optionContext = "disable-string-converter availableCommands", help = "Command name to provide help for")
            String buffer) {
        JLineShellComponent shell = ctx.getBean("shell", JLineShellComponent.class);
        SimpleParser parser = shell.getSimpleParser();
        parser.obtainHelp(buffer);
    }

    @CliCommand(value={"exit", "quit"}, help="Exits the shell")
    public ExitShellRequest quit() {
        return ExitShellRequest.NORMAL_EXIT;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
