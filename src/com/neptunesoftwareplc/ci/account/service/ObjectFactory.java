
package com.neptunesoftwareplc.ci.account.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.neptunesoftwareplc.ci.account.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FindAccountBalance_QNAME = new QName("http://service.account.ci.neptunesoftwareplc.com/", "findAccountBalance");
    private final static QName _CreateAndActivateLoanAccountResponse_QNAME = new QName("http://service.account.ci.neptunesoftwareplc.com/", "createAndActivateLoanAccountResponse");
    private final static QName _CIException_QNAME = new QName("http://service.account.ci.neptunesoftwareplc.com/", "CIException");
    private final static QName _FindAccountMiniStatementResponse_QNAME = new QName("http://service.account.ci.neptunesoftwareplc.com/", "findAccountMiniStatementResponse");
    private final static QName _CreateAndActivateLoanAccount_QNAME = new QName("http://service.account.ci.neptunesoftwareplc.com/", "createAndActivateLoanAccount");
    private final static QName _FindAccountBalanceResponse_QNAME = new QName("http://service.account.ci.neptunesoftwareplc.com/", "findAccountBalanceResponse");
    private final static QName _FindAccountMiniStatement_QNAME = new QName("http://service.account.ci.neptunesoftwareplc.com/", "findAccountMiniStatement");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.neptunesoftwareplc.ci.account.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FindAccountBalance }
     * 
     */
    public FindAccountBalance createFindAccountBalance() {
        return new FindAccountBalance();
    }

    /**
     * Create an instance of {@link CreateAndActivateLoanAccountResponse }
     * 
     */
    public CreateAndActivateLoanAccountResponse createCreateAndActivateLoanAccountResponse() {
        return new CreateAndActivateLoanAccountResponse();
    }

    /**
     * Create an instance of {@link CIException }
     * 
     */
    public CIException createCIException() {
        return new CIException();
    }

    /**
     * Create an instance of {@link FindAccountMiniStatementResponse }
     * 
     */
    public FindAccountMiniStatementResponse createFindAccountMiniStatementResponse() {
        return new FindAccountMiniStatementResponse();
    }

    /**
     * Create an instance of {@link CreateAndActivateLoanAccount }
     * 
     */
    public CreateAndActivateLoanAccount createCreateAndActivateLoanAccount() {
        return new CreateAndActivateLoanAccount();
    }

    /**
     * Create an instance of {@link FindAccountBalanceResponse }
     * 
     */
    public FindAccountBalanceResponse createFindAccountBalanceResponse() {
        return new FindAccountBalanceResponse();
    }

    /**
     * Create an instance of {@link FindAccountMiniStatement }
     * 
     */
    public FindAccountMiniStatement createFindAccountMiniStatement() {
        return new FindAccountMiniStatement();
    }

    /**
     * Create an instance of {@link AccountStmtOutputCIData }
     * 
     */
    public AccountStmtOutputCIData createAccountStmtOutputCIData() {
        return new AccountStmtOutputCIData();
    }

    /**
     * Create an instance of {@link BaseRequestData }
     * 
     */
    public BaseRequestData createBaseRequestData() {
        return new BaseRequestData();
    }

    /**
     * Create an instance of {@link Credentials }
     * 
     */
    public Credentials createCredentials() {
        return new Credentials();
    }

    /**
     * Create an instance of {@link BaseResponseData }
     * 
     */
    public BaseResponseData createBaseResponseData() {
        return new BaseResponseData();
    }

    /**
     * Create an instance of {@link AccountBalanceResponseData }
     * 
     */
    public AccountBalanceResponseData createAccountBalanceResponseData() {
        return new AccountBalanceResponseData();
    }

    /**
     * Create an instance of {@link LoanAccountResponseCIData }
     * 
     */
    public LoanAccountResponseCIData createLoanAccountResponseCIData() {
        return new LoanAccountResponseCIData();
    }

    /**
     * Create an instance of {@link AccountStatementOutputData }
     * 
     */
    public AccountStatementOutputData createAccountStatementOutputData() {
        return new AccountStatementOutputData();
    }

    /**
     * Create an instance of {@link LoanAccountRequestCIData }
     * 
     */
    public LoanAccountRequestCIData createLoanAccountRequestCIData() {
        return new LoanAccountRequestCIData();
    }

    /**
     * Create an instance of {@link CiErrorData }
     * 
     */
    public CiErrorData createCiErrorData() {
        return new CiErrorData();
    }

    /**
     * Create an instance of {@link AccountBalanceRequestData }
     * 
     */
    public AccountBalanceRequestData createAccountBalanceRequestData() {
        return new AccountBalanceRequestData();
    }

    /**
     * Create an instance of {@link ArrayList }
     * 
     */
    public ArrayList createArrayList() {
        return new ArrayList();
    }

    /**
     * Create an instance of {@link AccountStatementRequestData }
     * 
     */
    public AccountStatementRequestData createAccountStatementRequestData() {
        return new AccountStatementRequestData();
    }

    /**
     * Create an instance of {@link CiRequestBaseObject }
     * 
     */
    public CiRequestBaseObject createCiRequestBaseObject() {
        return new CiRequestBaseObject();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAccountBalance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.account.ci.neptunesoftwareplc.com/", name = "findAccountBalance")
    public JAXBElement<FindAccountBalance> createFindAccountBalance(FindAccountBalance value) {
        return new JAXBElement<FindAccountBalance>(_FindAccountBalance_QNAME, FindAccountBalance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAndActivateLoanAccountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.account.ci.neptunesoftwareplc.com/", name = "createAndActivateLoanAccountResponse")
    public JAXBElement<CreateAndActivateLoanAccountResponse> createCreateAndActivateLoanAccountResponse(CreateAndActivateLoanAccountResponse value) {
        return new JAXBElement<CreateAndActivateLoanAccountResponse>(_CreateAndActivateLoanAccountResponse_QNAME, CreateAndActivateLoanAccountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CIException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.account.ci.neptunesoftwareplc.com/", name = "CIException")
    public JAXBElement<CIException> createCIException(CIException value) {
        return new JAXBElement<CIException>(_CIException_QNAME, CIException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAccountMiniStatementResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.account.ci.neptunesoftwareplc.com/", name = "findAccountMiniStatementResponse")
    public JAXBElement<FindAccountMiniStatementResponse> createFindAccountMiniStatementResponse(FindAccountMiniStatementResponse value) {
        return new JAXBElement<FindAccountMiniStatementResponse>(_FindAccountMiniStatementResponse_QNAME, FindAccountMiniStatementResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAndActivateLoanAccount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.account.ci.neptunesoftwareplc.com/", name = "createAndActivateLoanAccount")
    public JAXBElement<CreateAndActivateLoanAccount> createCreateAndActivateLoanAccount(CreateAndActivateLoanAccount value) {
        return new JAXBElement<CreateAndActivateLoanAccount>(_CreateAndActivateLoanAccount_QNAME, CreateAndActivateLoanAccount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAccountBalanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.account.ci.neptunesoftwareplc.com/", name = "findAccountBalanceResponse")
    public JAXBElement<FindAccountBalanceResponse> createFindAccountBalanceResponse(FindAccountBalanceResponse value) {
        return new JAXBElement<FindAccountBalanceResponse>(_FindAccountBalanceResponse_QNAME, FindAccountBalanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAccountMiniStatement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.account.ci.neptunesoftwareplc.com/", name = "findAccountMiniStatement")
    public JAXBElement<FindAccountMiniStatement> createFindAccountMiniStatement(FindAccountMiniStatement value) {
        return new JAXBElement<FindAccountMiniStatement>(_FindAccountMiniStatement_QNAME, FindAccountMiniStatement.class, null, value);
    }

}
