package io.elastic.jdee1

import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import javax.json.Json
import javax.json.JsonObject
import javax.json.JsonObjectBuilder

@Ignore
class FunctionParamsProviderSpec extends Specification {

    JsonObjectBuilder configuration = Json.createObjectBuilder()
    @Shared
    def server = "JDE92ENT"
    @Shared
    def port = "6017"
    @Shared
    def user = "JDE"
    @Shared
    def password = "jde"
    @Shared
    def environment = "JDV920"
    @Shared
    def function = "AddressBookMasterMBF"

    def setup() {

    }

    def "get metadata model, given json object"() {
        configuration.add("server", server)
                .add("port", port)
                .add("user", user)
                .add("password", password)
                .add("environment", environment)
                .add("name", function)

        FunctionParamsProvider provider = new FunctionParamsProvider()
        JsonObject meta = provider.getMetaModel(configuration.build())
        print meta
        expect:
        meta.toString() == "{\"out\":{\"type\":\"object\",\"properties\":{\"\":{\"title\":\"\",\"type\":\"string\"},\"cActionCode\":{\"title\":\"cActionCode\",\"type\":\"string\"},\"mnAddressBookNumber\":{\"title\":\"mnAddressBookNumber\",\"type\":\"string\"},\"szLongAddressNumber\":{\"title\":\"szLongAddressNumber\",\"type\":\"string\"},\"szTaxId\":{\"title\":\"szTaxId\",\"type\":\"string\"},\"szSearchType\":{\"title\":\"szSearchType\",\"type\":\"string\"},\"szAlphaName\":{\"title\":\"szAlphaName\",\"type\":\"string\"},\"szSecondaryAlphaName\":{\"title\":\"szSecondaryAlphaName\",\"type\":\"string\"},\"szMailingName\":{\"title\":\"szMailingName\",\"type\":\"string\"},\"szSecondaryMailingName\":{\"title\":\"szSecondaryMailingName\",\"type\":\"string\"},\"szDescriptionCompressed\":{\"title\":\"szDescriptionCompressed\",\"type\":\"string\"},\"szBusinessUnit\":{\"title\":\"szBusinessUnit\",\"type\":\"string\"},\"szAddressLine1\":{\"title\":\"szAddressLine1\",\"type\":\"string\"},\"szAddressLine2\":{\"title\":\"szAddressLine2\",\"type\":\"string\"},\"szAddressLine3\":{\"title\":\"szAddressLine3\",\"type\":\"string\"},\"szAddressLine4\":{\"title\":\"szAddressLine4\",\"type\":\"string\"},\"szPostalCode\":{\"title\":\"szPostalCode\",\"type\":\"string\"},\"szCity\":{\"title\":\"szCity\",\"type\":\"string\"},\"szCounty\":{\"title\":\"szCounty\",\"type\":\"string\"},\"szState\":{\"title\":\"szState\",\"type\":\"string\"},\"szCountry\":{\"title\":\"szCountry\",\"type\":\"string\"},\"szPrefix1\":{\"title\":\"szPrefix1\",\"type\":\"string\"},\"szPhoneNumber1\":{\"title\":\"szPhoneNumber1\",\"type\":\"string\"},\"szPhoneNumberType1\":{\"title\":\"szPhoneNumberType1\",\"type\":\"string\"},\"cPayablesYNM\":{\"title\":\"cPayablesYNM\",\"type\":\"string\"},\"cReceivablesYN\":{\"title\":\"cReceivablesYN\",\"type\":\"string\"},\"cEmployeeYN\":{\"title\":\"cEmployeeYN\",\"type\":\"string\"},\"cUserCode\":{\"title\":\"cUserCode\",\"type\":\"string\"},\"cARAPNettingY\":{\"title\":\"cARAPNettingY\",\"type\":\"string\"},\"cSubledgerInactiveCode\":{\"title\":\"cSubledgerInactiveCode\",\"type\":\"string\"},\"cPersonCorporationCode\":{\"title\":\"cPersonCorporationCode\",\"type\":\"string\"},\"szCertificate\":{\"title\":\"szCertificate\",\"type\":\"string\"},\"szAddlIndTaxID\":{\"title\":\"szAddlIndTaxID\",\"type\":\"string\"},\"szCreditMessage\":{\"title\":\"szCreditMessage\",\"type\":\"string\"},\"szLanguage\":{\"title\":\"szLanguage\",\"type\":\"string\"},\"szIndustryClassification\":{\"title\":\"szIndustryClassification\",\"type\":\"string\"},\"cEMail\":{\"title\":\"cEMail\",\"type\":\"string\"},\"mn1stAddressNumber\":{\"title\":\"mn1stAddressNumber\",\"type\":\"string\"},\"mn2ndAddressNumber\":{\"title\":\"mn2ndAddressNumber\",\"type\":\"string\"},\"mn3rdAddressNumber\":{\"title\":\"mn3rdAddressNumber\",\"type\":\"string\"},\"mn4thAddressNumber\":{\"title\":\"mn4thAddressNumber\",\"type\":\"string\"},\"mn5thAddressNumber\":{\"title\":\"mn5thAddressNumber\",\"type\":\"string\"},\"mnFactorSpecialPayee\":{\"title\":\"mnFactorSpecialPayee\",\"type\":\"string\"},\"cAddressType3YN\":{\"title\":\"cAddressType3YN\",\"type\":\"string\"},\"cAddressType4YN\":{\"title\":\"cAddressType4YN\",\"type\":\"string\"},\"cAddressType5YN\":{\"title\":\"cAddressType5YN\",\"type\":\"string\"},\"szCategoryCode01\":{\"title\":\"szCategoryCode01\",\"type\":\"string\"},\"szAccountRepresentative\":{\"title\":\"szAccountRepresentative\",\"type\":\"string\"},\"szCategoryCode03\":{\"title\":\"szCategoryCode03\",\"type\":\"string\"},\"szGeographicRegion\":{\"title\":\"szGeographicRegion\",\"type\":\"string\"},\"szCategoryCode05\":{\"title\":\"szCategoryCode05\",\"type\":\"string\"},\"szCategoryCode06\":{\"title\":\"szCategoryCode06\",\"type\":\"string\"},\"sz1099Reporting\":{\"title\":\"sz1099Reporting\",\"type\":\"string\"},\"szCategoryCode08\":{\"title\":\"szCategoryCode08\",\"type\":\"string\"},\"szCategoryCode09\":{\"title\":\"szCategoryCode09\",\"type\":\"string\"},\"szCategoryCode10\":{\"title\":\"szCategoryCode10\",\"type\":\"string\"},\"szSalesRegion\":{\"title\":\"szSalesRegion\",\"type\":\"string\"},\"szCategoryCode12\":{\"title\":\"szCategoryCode12\",\"type\":\"string\"},\"szLineOfBusiness\":{\"title\":\"szLineOfBusiness\",\"type\":\"string\"},\"szSalesVolume\":{\"title\":\"szSalesVolume\",\"type\":\"string\"},\"szCategoryCode15\":{\"title\":\"szCategoryCode15\",\"type\":\"string\"},\"szCategoryCode16\":{\"title\":\"szCategoryCode16\",\"type\":\"string\"},\"szCategoryCode17\":{\"title\":\"szCategoryCode17\",\"type\":\"string\"},\"szCategoryCode18\":{\"title\":\"szCategoryCode18\",\"type\":\"string\"},\"szCategoryCode19\":{\"title\":\"szCategoryCode19\",\"type\":\"string\"},\"szCategoryCode20\":{\"title\":\"szCategoryCode20\",\"type\":\"string\"},\"szCategoryCode21\":{\"title\":\"szCategoryCode21\",\"type\":\"string\"},\"szCategoryCode22\":{\"title\":\"szCategoryCode22\",\"type\":\"string\"},\"szCategoryCode23\":{\"title\":\"szCategoryCode23\",\"type\":\"string\"},\"szCategoryCode24\":{\"title\":\"szCategoryCode24\",\"type\":\"string\"},\"szCategoryCode25\":{\"title\":\"szCategoryCode25\",\"type\":\"string\"},\"szCategoryCode26\":{\"title\":\"szCategoryCode26\",\"type\":\"string\"},\"szCategoryCode27\":{\"title\":\"szCategoryCode27\",\"type\":\"string\"},\"szCategoryCode28\":{\"title\":\"szCategoryCode28\",\"type\":\"string\"},\"szCategoryCode29\":{\"title\":\"szCategoryCode29\",\"type\":\"string\"},\"szCategoryCode30\":{\"title\":\"szCategoryCode30\",\"type\":\"string\"},\"szGlBankAccount\":{\"title\":\"szGlBankAccount\",\"type\":\"string\"},\"cClearedY\":{\"title\":\"cClearedY\",\"type\":\"string\"},\"szRemark\":{\"title\":\"szRemark\",\"type\":\"string\"},\"szUserReservedCode\":{\"title\":\"szUserReservedCode\",\"type\":\"string\"},\"mnUserReservedAmount\":{\"title\":\"mnUserReservedAmount\",\"type\":\"string\"},\"szUserReservedReference\":{\"title\":\"szUserReservedReference\",\"type\":\"string\"},\"szProgramId\":{\"title\":\"szProgramId\",\"type\":\"string\"},\"szRemark1\":{\"title\":\"szRemark1\",\"type\":\"string\"},\"cEdiSuccessfullyProcess\":{\"title\":\"cEdiSuccessfullyProcess\",\"type\":\"string\"},\"szShortcutClientType\":{\"title\":\"szShortcutClientType\",\"type\":\"string\"},\"szTicker\":{\"title\":\"szTicker\",\"type\":\"string\"},\"szStockExchange\":{\"title\":\"szStockExchange\",\"type\":\"string\"},\"szDUNSNumber\":{\"title\":\"szDUNSNumber\",\"type\":\"string\"},\"szClassificationCode01\":{\"title\":\"szClassificationCode01\",\"type\":\"string\"},\"szClassificationCode02\":{\"title\":\"szClassificationCode02\",\"type\":\"string\"},\"szClassificationCode03\":{\"title\":\"szClassificationCode03\",\"type\":\"string\"},\"szClassificationCode04\":{\"title\":\"szClassificationCode04\",\"type\":\"string\"},\"szClassificationCode05\":{\"title\":\"szClassificationCode05\",\"type\":\"string\"},\"szYearStarted\":{\"title\":\"szYearStarted\",\"type\":\"string\"},\"szEmployeeGroupApprovals\":{\"title\":\"szEmployeeGroupApprovals\",\"type\":\"string\"},\"cIndicatorFlg\":{\"title\":\"cIndicatorFlg\",\"type\":\"string\"},\"szRevenueRange\":{\"title\":\"szRevenueRange\",\"type\":\"string\"}}},\"in\":{\"type\":\"object\",\"properties\":{\"\":{\"title\":\"\",\"type\":\"string\"},\"cActionCode\":{\"title\":\"cActionCode\",\"type\":\"string\"},\"mnAddressBookNumber\":{\"title\":\"mnAddressBookNumber\",\"type\":\"string\"},\"szLongAddressNumber\":{\"title\":\"szLongAddressNumber\",\"type\":\"string\"},\"szTaxId\":{\"title\":\"szTaxId\",\"type\":\"string\"},\"szSearchType\":{\"title\":\"szSearchType\",\"type\":\"string\"},\"szAlphaName\":{\"title\":\"szAlphaName\",\"type\":\"string\"},\"szSecondaryAlphaName\":{\"title\":\"szSecondaryAlphaName\",\"type\":\"string\"},\"szMailingName\":{\"title\":\"szMailingName\",\"type\":\"string\"},\"szSecondaryMailingName\":{\"title\":\"szSecondaryMailingName\",\"type\":\"string\"},\"szDescriptionCompressed\":{\"title\":\"szDescriptionCompressed\",\"type\":\"string\"},\"szBusinessUnit\":{\"title\":\"szBusinessUnit\",\"type\":\"string\"},\"szAddressLine1\":{\"title\":\"szAddressLine1\",\"type\":\"string\"},\"szAddressLine2\":{\"title\":\"szAddressLine2\",\"type\":\"string\"},\"szAddressLine3\":{\"title\":\"szAddressLine3\",\"type\":\"string\"},\"szAddressLine4\":{\"title\":\"szAddressLine4\",\"type\":\"string\"},\"szPostalCode\":{\"title\":\"szPostalCode\",\"type\":\"string\"},\"szCity\":{\"title\":\"szCity\",\"type\":\"string\"},\"szCounty\":{\"title\":\"szCounty\",\"type\":\"string\"},\"szState\":{\"title\":\"szState\",\"type\":\"string\"},\"szCountry\":{\"title\":\"szCountry\",\"type\":\"string\"},\"szPrefix1\":{\"title\":\"szPrefix1\",\"type\":\"string\"},\"szPhoneNumber1\":{\"title\":\"szPhoneNumber1\",\"type\":\"string\"},\"szPhoneNumberType1\":{\"title\":\"szPhoneNumberType1\",\"type\":\"string\"},\"cPayablesYNM\":{\"title\":\"cPayablesYNM\",\"type\":\"string\"},\"cReceivablesYN\":{\"title\":\"cReceivablesYN\",\"type\":\"string\"},\"cEmployeeYN\":{\"title\":\"cEmployeeYN\",\"type\":\"string\"},\"cUserCode\":{\"title\":\"cUserCode\",\"type\":\"string\"},\"cARAPNettingY\":{\"title\":\"cARAPNettingY\",\"type\":\"string\"},\"cSubledgerInactiveCode\":{\"title\":\"cSubledgerInactiveCode\",\"type\":\"string\"},\"cPersonCorporationCode\":{\"title\":\"cPersonCorporationCode\",\"type\":\"string\"},\"szCertificate\":{\"title\":\"szCertificate\",\"type\":\"string\"},\"szAddlIndTaxID\":{\"title\":\"szAddlIndTaxID\",\"type\":\"string\"},\"szCreditMessage\":{\"title\":\"szCreditMessage\",\"type\":\"string\"},\"szLanguage\":{\"title\":\"szLanguage\",\"type\":\"string\"},\"szIndustryClassification\":{\"title\":\"szIndustryClassification\",\"type\":\"string\"},\"cEMail\":{\"title\":\"cEMail\",\"type\":\"string\"},\"mn1stAddressNumber\":{\"title\":\"mn1stAddressNumber\",\"type\":\"string\"},\"mn2ndAddressNumber\":{\"title\":\"mn2ndAddressNumber\",\"type\":\"string\"},\"mn3rdAddressNumber\":{\"title\":\"mn3rdAddressNumber\",\"type\":\"string\"},\"mn4thAddressNumber\":{\"title\":\"mn4thAddressNumber\",\"type\":\"string\"},\"mn5thAddressNumber\":{\"title\":\"mn5thAddressNumber\",\"type\":\"string\"},\"mnFactorSpecialPayee\":{\"title\":\"mnFactorSpecialPayee\",\"type\":\"string\"},\"cAddressType3YN\":{\"title\":\"cAddressType3YN\",\"type\":\"string\"},\"cAddressType4YN\":{\"title\":\"cAddressType4YN\",\"type\":\"string\"},\"cAddressType5YN\":{\"title\":\"cAddressType5YN\",\"type\":\"string\"},\"szCategoryCode01\":{\"title\":\"szCategoryCode01\",\"type\":\"string\"},\"szAccountRepresentative\":{\"title\":\"szAccountRepresentative\",\"type\":\"string\"},\"szCategoryCode03\":{\"title\":\"szCategoryCode03\",\"type\":\"string\"},\"szGeographicRegion\":{\"title\":\"szGeographicRegion\",\"type\":\"string\"},\"szCategoryCode05\":{\"title\":\"szCategoryCode05\",\"type\":\"string\"},\"szCategoryCode06\":{\"title\":\"szCategoryCode06\",\"type\":\"string\"},\"sz1099Reporting\":{\"title\":\"sz1099Reporting\",\"type\":\"string\"},\"szCategoryCode08\":{\"title\":\"szCategoryCode08\",\"type\":\"string\"},\"szCategoryCode09\":{\"title\":\"szCategoryCode09\",\"type\":\"string\"},\"szCategoryCode10\":{\"title\":\"szCategoryCode10\",\"type\":\"string\"},\"szSalesRegion\":{\"title\":\"szSalesRegion\",\"type\":\"string\"},\"szCategoryCode12\":{\"title\":\"szCategoryCode12\",\"type\":\"string\"},\"szLineOfBusiness\":{\"title\":\"szLineOfBusiness\",\"type\":\"string\"},\"szSalesVolume\":{\"title\":\"szSalesVolume\",\"type\":\"string\"},\"szCategoryCode15\":{\"title\":\"szCategoryCode15\",\"type\":\"string\"},\"szCategoryCode16\":{\"title\":\"szCategoryCode16\",\"type\":\"string\"},\"szCategoryCode17\":{\"title\":\"szCategoryCode17\",\"type\":\"string\"},\"szCategoryCode18\":{\"title\":\"szCategoryCode18\",\"type\":\"string\"},\"szCategoryCode19\":{\"title\":\"szCategoryCode19\",\"type\":\"string\"},\"szCategoryCode20\":{\"title\":\"szCategoryCode20\",\"type\":\"string\"},\"szCategoryCode21\":{\"title\":\"szCategoryCode21\",\"type\":\"string\"},\"szCategoryCode22\":{\"title\":\"szCategoryCode22\",\"type\":\"string\"},\"szCategoryCode23\":{\"title\":\"szCategoryCode23\",\"type\":\"string\"},\"szCategoryCode24\":{\"title\":\"szCategoryCode24\",\"type\":\"string\"},\"szCategoryCode25\":{\"title\":\"szCategoryCode25\",\"type\":\"string\"},\"szCategoryCode26\":{\"title\":\"szCategoryCode26\",\"type\":\"string\"},\"szCategoryCode27\":{\"title\":\"szCategoryCode27\",\"type\":\"string\"},\"szCategoryCode28\":{\"title\":\"szCategoryCode28\",\"type\":\"string\"},\"szCategoryCode29\":{\"title\":\"szCategoryCode29\",\"type\":\"string\"},\"szCategoryCode30\":{\"title\":\"szCategoryCode30\",\"type\":\"string\"},\"szGlBankAccount\":{\"title\":\"szGlBankAccount\",\"type\":\"string\"},\"cClearedY\":{\"title\":\"cClearedY\",\"type\":\"string\"},\"szRemark\":{\"title\":\"szRemark\",\"type\":\"string\"},\"szUserReservedCode\":{\"title\":\"szUserReservedCode\",\"type\":\"string\"},\"mnUserReservedAmount\":{\"title\":\"mnUserReservedAmount\",\"type\":\"string\"},\"szUserReservedReference\":{\"title\":\"szUserReservedReference\",\"type\":\"string\"},\"szProgramId\":{\"title\":\"szProgramId\",\"type\":\"string\"},\"szRemark1\":{\"title\":\"szRemark1\",\"type\":\"string\"},\"cEdiSuccessfullyProcess\":{\"title\":\"cEdiSuccessfullyProcess\",\"type\":\"string\"},\"szShortcutClientType\":{\"title\":\"szShortcutClientType\",\"type\":\"string\"},\"szTicker\":{\"title\":\"szTicker\",\"type\":\"string\"},\"szStockExchange\":{\"title\":\"szStockExchange\",\"type\":\"string\"},\"szDUNSNumber\":{\"title\":\"szDUNSNumber\",\"type\":\"string\"},\"szClassificationCode01\":{\"title\":\"szClassificationCode01\",\"type\":\"string\"},\"szClassificationCode02\":{\"title\":\"szClassificationCode02\",\"type\":\"string\"},\"szClassificationCode03\":{\"title\":\"szClassificationCode03\",\"type\":\"string\"},\"szClassificationCode04\":{\"title\":\"szClassificationCode04\",\"type\":\"string\"},\"szClassificationCode05\":{\"title\":\"szClassificationCode05\",\"type\":\"string\"},\"szYearStarted\":{\"title\":\"szYearStarted\",\"type\":\"string\"},\"szEmployeeGroupApprovals\":{\"title\":\"szEmployeeGroupApprovals\",\"type\":\"string\"},\"cIndicatorFlg\":{\"title\":\"cIndicatorFlg\",\"type\":\"string\"},\"szRevenueRange\":{\"title\":\"szRevenueRange\",\"type\":\"string\"}}}}"
    }
}