
package org.hudsonci.update.client.model;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Model representing Update Center signature 
 * @author Winston Prakash
 */
public class Signature {
    
    private List<String> certificates = new ArrayList<String>();
    private String digest;
    private String signature;
    private String correct_digest;
    private String correct_signature;

    public List<String> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<String> certificates) {
        this.certificates = certificates;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCorrect_digest() {
        return correct_digest;
    }

    public void setCorrect_digest(String correct_digest) {
        this.correct_digest = correct_digest;
    }

    public String getCorrect_signature() {
        return correct_signature;
    }

    public void setCorrect_signature(String correct_signature) {
        this.correct_signature = correct_signature;
    }
}
