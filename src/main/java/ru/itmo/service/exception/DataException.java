package ru.itmo.service.exception;

import javax.xml.ws.WebFault;

@WebFault(name = "DataException", faultBean = "ru.itmo.service.exception.DataException.FaultInfo")
public class DataException extends Exception {
    private final DataFaultInfo faultInfo;

    public DataException(Long entityId, String message) {
        super(message);
        this.faultInfo = new DataFaultInfo(entityId, message);
    }

    public DataException(String message, DataFaultInfo faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public DataFaultInfo getFaultInfo() {
        return faultInfo;
    }

    public static class DataFaultInfo {
        private Long entityId;
        private String message;

        public DataFaultInfo(Long entityId, String message) {
            this.entityId = entityId;
            this.message = message;
        }

        public DataFaultInfo() {
        }

        public Long getEntityId() {
            return entityId;
        }

        public void setEntityId(Long entityId) {
            this.entityId = entityId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}