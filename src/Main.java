import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import org.joda.time.DateTime;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.impl.AuthnRequestBuilder;
import org.opensaml.security.credential.BasicCredential;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.X509Certificate;
import org.opensaml.xmlsec.signature.X509Data;
import org.opensaml.xmlsec.signature.impl.KeyInfoBuilder;
import org.opensaml.xmlsec.signature.impl.X509CertificateBuilder;
import org.opensaml.xmlsec.signature.impl.X509DataBuilder;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.Signer;

public class Main {

	public static void main(String[] args) throws Exception {
		InitializationService.initialize();

		PublicKey pubKey = SecurityUtils.loadPublicKey();
		PrivateKey privKey = SecurityUtils.loadPrivateKey();

		BasicCredential signingCredential = CredentialSupport.getSimpleCredential(pubKey, privKey);

		KeyInfo keyInfo = new KeyInfoBuilder().buildObject();
		X509Certificate x509Certificate = new X509CertificateBuilder().buildObject();
		x509Certificate.setValue(Base64.getEncoder().encodeToString(pubKey.getEncoded()));
		X509Data x509Data = new X509DataBuilder().buildObject();
		x509Data.getX509Certificates().add(x509Certificate);
		keyInfo.getX509Datas().add(x509Data);

		Signature signature = OpenSAMLUtils.buildSAMLObject(Signature.class);
		signature.setSigningCredential(signingCredential);
		signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
		signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
		signature.setKeyInfo(keyInfo);

		AuthnRequest request = new AuthnRequestBuilder().buildObject();
		request.setID("theId");
		request.setIssueInstant(new DateTime());
		request.setSignature(signature);
		try {
			XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(request).marshall(request);
			Signer.signObject(signature);
			System.out.println(OpenSAMLUtils.toString(request));
		} catch (MarshallingException e) {
			throw new RuntimeException(e);
		}
	}

}
