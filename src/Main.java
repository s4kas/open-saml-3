import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.joda.time.DateTime;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.impl.AuthnRequestBuilder;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.security.x509.BasicX509Credential;
import org.opensaml.xmlsec.SignatureSigningParameters;
import org.opensaml.xmlsec.keyinfo.KeyInfoGenerator;
import org.opensaml.xmlsec.keyinfo.impl.X509KeyInfoGeneratorFactory;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureSupport;

import net.shibboleth.utilities.java.support.xml.SerializeSupport;

public class Main {

	public static void main(String[] args) throws Exception {
		InitializationService.initialize();

		PrivateKey privKey = SecurityUtils.loadPrivateKey();
		X509Certificate certificate = SecurityUtils.loadCertificate();

		BasicX509Credential signingCredential = CredentialSupport.getSimpleCredential(certificate, privKey);

		X509KeyInfoGeneratorFactory x509KeyInfoGeneratorFactory = new X509KeyInfoGeneratorFactory();
		x509KeyInfoGeneratorFactory.setEmitEntityCertificate(true);
		KeyInfoGenerator keyInfoGenerator = x509KeyInfoGeneratorFactory.newInstance();
		
		SignatureSigningParameters signatureSigningParameters = new SignatureSigningParameters();
		signatureSigningParameters.setSigningCredential(signingCredential);
		signatureSigningParameters.setKeyInfoGenerator(keyInfoGenerator);
		signatureSigningParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
		signatureSigningParameters.setSignatureCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

		AuthnRequest request = new AuthnRequestBuilder().buildObject();
		request.setID("theId");
		request.setIssueInstant(new DateTime());
		try {
			SignatureSupport.signObject(request, signatureSigningParameters);
			System.out.println(SerializeSupport.nodeToString(request.getDOM()));
		} catch (MarshallingException e) {
			throw new RuntimeException(e);
		}
	}

}
