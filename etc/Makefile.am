include $(top_srcdir)/config/utility.am

confdir = $(SIPX_CONFDIR)/sipxcallqueue
dist_conf_DATA = \
	$(wilcard $(srcdir)/sipxcallqueue/*.*)

fsdir = $(SIPX_CONFDIR)/freeswitch
dist_fs_DATA = \
	$(wilcard $(srcdir)/freeswitch/*.*)

precommit : validate-xml

validate-xml : $(wilcard $(srcdir)/sipxcallqueue/*.xml)
	xmllint --noout $^
