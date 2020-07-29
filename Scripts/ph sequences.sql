DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHA_ALERT;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHA_ALERT
  START WITH 39
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHA_ALERTS;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHA_ALERTS
  START WITH 685666
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHA_MESSAGE;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHA_MESSAGE
  START WITH 706624
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHB_PAYMENT;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHB_PAYMENT
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_CHARGE;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_CHARGE
  START WITH 136
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_DEDUCTION;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_DEDUCTION
  START WITH 740
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_SPLIT;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_SPLIT
  START WITH 45856
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_TIER;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_TIER
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_VALUE;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_VALUE
  START WITH 517
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_WAIVER;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHC_WAIVER
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHG_WF_LOG;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHG_WF_LOG
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_ENRL_LOG;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_ENRL_LOG
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_FILTER;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_FILTER
  START WITH 493
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_FRIEND;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_FRIEND
  START WITH 28
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_LIST;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_LIST
  START WITH 51
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_SETTING;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_SETTING
  START WITH 173
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_TEMPLATE;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_TEMPLATE
  START WITH 145
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_TIER;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_TIER
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_TXN_LOG;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHL_TXN_LOG
  START WITH 9971
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 5
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHS_EST_LOG;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHS_EST_LOG
  START WITH 619
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 3
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHS_TASK;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHS_TASK
  START WITH 2
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


DROP SEQUENCE CHANNELMANAGERFEP.SEQ_PHT_ACCOUNT;

CREATE SEQUENCE CHANNELMANAGERFEP.SEQ_PHT_ACCOUNT
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 2
  NOORDER;


GRANT SELECT ON CHANNELMANAGERFEP.SEQ_PHA_ALERTS TO BERHANLIVE;
