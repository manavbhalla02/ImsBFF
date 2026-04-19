package com.ims.bff.authZ.orgDiscovery.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.orgDiscovery.enums.OtpDeliveryChannel;
import com.ims.bff.authZ.orgDiscovery.exception.OrgDiscoveryException;
import com.ims.bff.authZ.orgDiscovery.provider.OtpDeliveryProvider;

@Component
public class OtpDeliveryProviderFactory {

    private final Map<OtpDeliveryChannel, OtpDeliveryProvider> providersByChannel;

    public OtpDeliveryProviderFactory(List<OtpDeliveryProvider> providers) {
        this.providersByChannel = providers.stream()
                .collect(Collectors.toMap(OtpDeliveryProvider::supports, Function.identity()));
    }

    public OtpDeliveryProvider getProvider(OtpDeliveryChannel channel) {
        OtpDeliveryProvider provider = providersByChannel.get(channel);
        if (provider == null) {
            throw new OrgDiscoveryException("No OTP delivery provider configured for channel " + channel);
        }
        return provider;
    }
}
