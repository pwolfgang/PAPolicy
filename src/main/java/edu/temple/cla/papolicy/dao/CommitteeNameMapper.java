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

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * Map queries of the CommitteeNames table to a CommitteeName object.
 * @author Paul Wolfgang
 */
public class CommitteeNameMapper implements ParameterizedRowMapper<CommitteeName>{

    private static final Logger logger = Logger.getLogger(CommitteeAliasMapper.class);

    /**
     * Perform the mapping.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
    @Override
    public CommitteeName mapRow(ResultSet rs, int rowNum) throws SQLException {

        CommitteeName item = new CommitteeName();
        try {
        item.setCtyCode(rs.getInt("CtyCode"));
        item.setName(rs.getString("Name"));
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
        return item;
    }

    /**
     * Determine of two ParameterizedRowmapper objects are equal.  Since
     * the row mapper is stateless, equality of class is sufficient.
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        return (this.getClass() == o.getClass());
    }

    /**
     * Since all RowMapper objects of the same class are equal,
     * the hashCode is the hashCode of the class object.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
