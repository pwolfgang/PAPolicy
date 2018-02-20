/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.temple.cla.papolicy.dao;

import java.util.List;

/**
 * A topic consists of a major topic and a list of subtopics for each
 * major topic.
 * @author Paul Wolfgang
 */
public class Topic {

    private int code;
    private String description;
    private List<Topic> subTopics;

    /**
     * The unique topic code
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * The unique topic code
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * The text to display for this topic
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * The text to display for this topic
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The list of subtopics.
     * @return the subTopics
     */
    public List<Topic> getSubTopics() {
        return subTopics;
    }

    /**
     * The list of subtopics.
     * @param subTopics the subTopics to set
     */
    public void setSubTopics(List<Topic> subTopics) {
        this.subTopics = subTopics;
    }

}
