include $(top_srcdir)/config/utility.am

confdir = $(SIPX_CONFDIR)/sipxcallqueue

xml = \
	sipxcallqueue/CallQueueAgent.xml \
	sipxcallqueue/CallQueue.xml \
	sipxcallqueue/CallQueueSettings.xml

dist_conf_DATA = \
	$(xml) \
	sipxcallqueue/CallQueueAgent.properties \
	sipxcallqueue/CallQueue.properties \
	sipxcallqueue/CallQueueSettings.properties


fsdir = $(SIPX_CONFDIR)/freeswitch
dist_fs_DATA = \
	freeswitch/callcenter.conf.xml.vm

precommit : validate-xml

validate-xml : $(xml)
	xmllint --noout $^
