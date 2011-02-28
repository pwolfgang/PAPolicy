/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 *
 * @author Paul Wolfgang
 */
public class TranscriptCommittee {
    private int transcriptID;
    private CommitteeAlias committeeAlias;

    /**
     * @return the transcriptID
     */
    public int getTranscriptID() {
        return transcriptID;
    }

    /**
     * @param transcriptID the transcriptID to set
     */
    public void setTranscriptID(int transcriptID) {
        this.transcriptID = transcriptID;
    }

    /**
     * @return the committeeAlias
     */
    public CommitteeAlias getCommitteeAlias() {
        return committeeAlias;
    }

    /**
     * @param committeeAlias the committeeAlias to set
     */
    public void setCommitteeAlias(CommitteeAlias committeeAlias) {
        this.committeeAlias = committeeAlias;
    }



}
