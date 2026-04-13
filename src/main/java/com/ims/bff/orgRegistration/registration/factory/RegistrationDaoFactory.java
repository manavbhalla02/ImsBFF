package com.ims.bff.orgRegistration.registration.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.common.dao.TypedDao;
import com.ims.bff.common.factory.AbstractComponentFactory;
import com.ims.bff.orgRegistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgRegistration.registration.exception.RegistrationComponentNotFoundException;

@Component
public class RegistrationDaoFactory extends AbstractComponentFactory<
        RegistrationStepType,
        TypedDao<RegistrationStepType, ?>> {

    public RegistrationDaoFactory(List<TypedDao<RegistrationStepType, ?>> daos) {
        super(daos);
    }

    @SuppressWarnings("unchecked")
    public <T> TypedDao<RegistrationStepType, T> getDao(RegistrationStepType stepType) {
        return (TypedDao<RegistrationStepType, T>) getComponent(
                stepType,
                new RegistrationComponentNotFoundException("DAO", stepType));
    }
}
