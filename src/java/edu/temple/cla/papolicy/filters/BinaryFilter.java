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
"            "+getDescription()+"\n"+
"            <br /><input type=\"radio\" name=\""+parameterName+"\" value=\""+BOTH+"\" checked=\"checked\" />&nbsp no filter\n"+
"                  <input type=\"radio\" name=\""+parameterName+"\" value=\"0\" />&nbsp Exclude\n"+
"                  <input type=\"radio\" name=\""+parameterName+"\" value=\"1\" />&nbsp Include";
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
