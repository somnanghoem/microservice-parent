package com.microservices.orderservice.type;

import com.util.responseutil.util.ResponseHeader;

public enum ResponseResultMessage {
    SUCCESS ( "0000", "Success"),
    USER_NOT_FOUND ( "1001", "User information not found"),
    TOKEN_NOT_FOUND( "1002", "Token information not found"),
    TOKEN_EXPIRED( "1003", "Token has been expired,please try to generate new token"),
    USER_NAME_EMPTY ( "1004","User name cannot be empty or null"),
    PASSWORD_EMPTY ( "1005", "Password cannot be empty or null"),
    INVALID_PASSWORD ( "1006", "Invalid password"),
    REGISTER_TOKEN_ERROR( "1007", "Register/Update user token information error"),
    MISSING_AUTHORIZATION( "1008", "Missing authorization "),
    GENERAL_ERROR( "9999", "General System Error");



    private String value;
    private String description;

    ResponseResultMessage(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }


    public static ResponseResultMessage resultOutputMessage( String value ) {
        ResponseResultMessage resultConst = null;
        if ( value != null ) {
            for ( ResponseResultMessage searchConst : values() ) {
                if ( searchConst.getValue().equals( value ) ) {
                    resultConst = searchConst;
                    break;
                }
            }
        }
        if ( resultConst == null ) {
            resultConst = resultOutputMessage( "9999" );
        }
        return resultConst;
    }

    public static ResponseHeader resultOutputMessage(Exception e ) {
        ResponseHeader header = new ResponseHeader();
        ResponseResultMessage resultMessageInfo = null;
        if ( e.getMessage().length() > 4 ) {
            resultMessageInfo = resultOutputMessage( "9999" );
        } else {
            resultMessageInfo = resultOutputMessage( e.getMessage() );
        }
        header.setSuccessYN("N");
        header.setResultCode(resultMessageInfo.getValue());
        header.setResultMessage(resultMessageInfo.getDescription());
        return  header;
    }
}
