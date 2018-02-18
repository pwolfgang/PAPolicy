/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 * The one to many relationship table for transcripts to committees.
 * @author Paul Wolfgang
 */
public class TranscriptCommittee {
    private int transcriptID;
    private CommitteeAlias committeeAlias;

    /**
     * The unique ID for this transcript record.
     * @return the transcriptID
     */
    public int getTranscriptID() {
        return transcriptID;
    }

    /**
     * The unique ID for this transcript record.
     * @param transcriptID the transcriptID to set
     */
    public void setTranscriptID(int transcriptID) {
        this.transcriptID = transcriptID;
    }

    /**
     * The committee alias of the committee
     * @return the committeeAlias
     */
    public CommitteeAlias getCommitteeAlias() {
        return committeeAlias;
    }

    /**
     * The committee alias of the committee
     * @param committeeAlias the committeeAlias to set
     */
    public void setCommitteeAlias(CommitteeAlias committeeAlias) {
        this.committeeAlias = committeeAlias;
    }



}
