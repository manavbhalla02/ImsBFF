package com.ims.bff.registration.exception;

public class DuplicateDomainException extends RuntimeException {

    public DuplicateDomainException(String domainName) {
        super("Domain is already registered: " + domainName);
    }
}
