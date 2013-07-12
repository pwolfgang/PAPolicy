package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
import edu.temple.cla.papolicy.queryBuilder.Expression;
import javax.servlet.http.HttpServletRequest;

/**
 * A binary filter presents the choice of either including or excluding 
 * a filter. It also has the option to ignore, which is the default.
 * @author Paul Wolfgang
 */
public class BinaryFilter extends Filter {

    private String parameterName;
    private String parameterValue;
    private static final String BOTH = "587";
    private String filterQualifier;

    public BinaryFilter(int id, int tableId, String description, 
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    @Override
    public String getFilterFormInput() {
        return "\n"+
"            <fieldset><legend>"+getDescription()+"</legend>\n"+
"            <input type=\"radio\" name=\""+parameterName+"\" id=\""+parameterName+"B\" value=\""+BOTH+"\" checked=\"checked\" />"
                + "&nbsp; <label for=\""+parameterName+"B\">no filter</label>\n"+
"                  <input type=\"radio\" name=\""+parameterName+"\" id=\""+parameterName+"0\" value=\"0\" />"
                + "&nbsp; <label for=\""+parameterName+"0\">Exclude</label>\n"+
"                  <input type=\"radio\" name=\""+parameterName+"\" id=\""+parameterName+"1\" value=\"1\" />"
                + "&nbsp; <label for=\""+parameterName+"1\">Include</label>\n" +
"             </fieldset>\n";
        }

    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        if (parameterValue != null) {
            buildFilterStrings();
        }
    }

    private void buildFilterStrings() {
        if (parameterValue.equals(BOTH)) {
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        } else if (parameterValue.equals("1")) {
            filterQuery = new Comparison(getColumnName(), "<>", "0");
            filterQualifier = "Include " + getDescription();
        } else {
            filterQuery = new Comparison(getColumnName(), "=", "0");
            filterQualifier = "Exclude " + getDescription();
        }
    }

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
