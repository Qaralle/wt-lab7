package ru.itmo.service.exception;

import javax.xml.ws.WebFault;

@WebFault(name = "ArgumentException", faultBean = "ru.itmo.service.exception.ArgumentException.FaultInfo")
public class ArgumentException extends Exception {
    private final ArgumentFaultInfo faultInfo;

    public ArgumentException(String argumentName, String message) {
        super(message);
        this.faultInfo = new ArgumentFaultInfo(argumentName, message);
    }

    public ArgumentException(String message, ArgumentFaultInfo faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public ArgumentFaultInfo getFaultInfo() {
        return faultInfo;
    }

    public static class ArgumentFaultInfo {
        private String argumentName;
        private String message;

        public ArgumentFaultInfo(String argumentName, String message) {
            this.argumentName = argumentName;
            this.message = message;
        }

        public ArgumentFaultInfo() {
        }

        public String getArgumentName() {
            return argumentName;
        }

        public void setArgumentName(String argumentName) {
            this.argumentName = argumentName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}