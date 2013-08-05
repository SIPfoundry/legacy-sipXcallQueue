/**
 *
 *
 * Copyright (c) 2013 eZuce, Inc. All rights reserved.
 * Contributed to SIPfoundry under a Contributor Agreement
 *
 * This software is free software; you can redistribute it and/or modify it under
 * the terms of the Affero General Public License (AGPL) as published by the
 * Free Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 */
package org.sipfoundry.sipxconfig.callqueue;

import org.sipfoundry.sipxconfig.address.AddressManager;
import org.sipfoundry.sipxconfig.commserver.Location;
import org.sipfoundry.sipxconfig.feature.FeatureManager;
import org.sipfoundry.sipxconfig.freeswitch.FreeswitchFeature;
import org.sipfoundry.sipxconfig.freeswitch.api.FreeswitchApi;
import org.sipfoundry.sipxconfig.xmlrpc.ApiProvider;
import org.springframework.beans.factory.annotation.Required;

public class CallQueueDeployer {
    private static final String CREATE_AGENT = "agent add agent-%s callback";
    private static final String SET_AGENT_STATUS = "agent set %s agent-%s '%s'";
    private static final String SET_AGENT_PROP = "agent set %s agent-%s %s";
    private static final String DELETE_AGENT = "agent del agent-%s";
    private static final String RELOAD_QUEUE = "queue reload queue-%s";
    private static final String UNLOAD_QUEUE = "queue unload queue-%s";
    private ApiProvider<FreeswitchApi> m_freeswitchApiProvider;
    private FeatureManager m_featureManager;
    private AddressManager m_addressManager;

    public void deployAgent(CallQueueAgent callQueueAgent, boolean isNew) {
        FreeswitchApi api = getFsApi();
        if (isNew) {
            api.callcenter_config(String.format(CREATE_AGENT, callQueueAgent.getExtension()));
        }
        api.callcenter_config(String.format(SET_AGENT_PROP, "contact", callQueueAgent.getExtension(),
                callQueueAgent.getContactUri()));
        api.callcenter_config(String.format(SET_AGENT_STATUS, "status", callQueueAgent.getExtension(),
                callQueueAgent.getSettingValue("call-queue-agent/status")));
        api.callcenter_config(String.format(SET_AGENT_PROP, "max_no_answer", callQueueAgent.getExtension(),
                callQueueAgent.getSettingValue("call-queue-agent/max-no-answer")));
        api.callcenter_config(String.format(SET_AGENT_PROP, "wrap_up_time", callQueueAgent.getExtension(),
                callQueueAgent.getSettingValue("call-queue-agent/wrap-up-time")));
        api.callcenter_config(String.format(SET_AGENT_PROP, "reject_delay_time", callQueueAgent.getExtension(),
                callQueueAgent.getSettingValue("call-queue-agent/reject-delay-time")));
        api.callcenter_config(String.format(SET_AGENT_PROP, "busy_delay_time", callQueueAgent.getExtension(),
                callQueueAgent.getSettingValue("call-queue-agent/busy-delay-time")));
        api.callcenter_config(String.format(SET_AGENT_PROP, "no_answer_delay_time", callQueueAgent.getExtension(),
                callQueueAgent.getSettingValue("call-queue-agent/no-answer-delay-time")));
    }

    public void deleteAgent(String extension) {
        getFsApi().callcenter_config(String.format(DELETE_AGENT, extension));
    }

    public void reloadQueue(String extension) {
        getFsApi().callcenter_config(String.format(RELOAD_QUEUE, extension));
    }

    public void deleteQueue(String extension) {
        getFsApi().callcenter_config(String.format(UNLOAD_QUEUE, extension));
    }

    private FreeswitchApi getFsApi() {
        Location enabledLocation = m_featureManager.getLocationsForEnabledFeature(CallQueueContext.FEATURE).get(0);
        String url = m_addressManager.getSingleAddress(FreeswitchFeature.XMLRPC_ADDRESS, enabledLocation).toString();
        return m_freeswitchApiProvider.getApi(url);
    }

    @Required
    public void setFreeswitchApiProvider(ApiProvider<FreeswitchApi> freeswitchApiProvider) {
        m_freeswitchApiProvider = freeswitchApiProvider;
    }

    @Required
    public void setFeatureManager(FeatureManager featureManager) {
        m_featureManager = featureManager;
    }

    @Required
    public void setAddressManager(AddressManager addressManager) {
        m_addressManager = addressManager;
    }

}
