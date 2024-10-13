/**
 * Scope.java
 *
 * Stores the variables in the current scope, and allows variables in parent/ancestor scopes to be accessed/updated.
 */

package runtime;

import java.util.HashMap;
import java.util.Map;

public class Scope {

  Scope parent;
  Map<String, RtVal> variables; // stores <varname, value> pairs

  public Scope(Scope parent) {
    this.parent = parent;
    this.variables = new HashMap<>();
  }

  /**
   * Declare a new variable with a given value.
   * Throws a RuntimeError if the variable name is already used in this scope.
   */
  public void declareVar(String varname, RtVal val) {
    if (variables.containsKey(varname)) {
      throw new RuntimeException(
          "Cannot declare variable " + varname + ". A variable with the same name already exists in this scope.");
    }
    variables.put(varname, val);
  }

  /**
   * Update the value of a variable if it has already been declared.
   * Throws a RuntimeError if the variable has not yet been declared.
   */
  public void updateVar(String varname, RtVal val) {
    Scope env = getVarEnv(varname);
    if (env == null) {
      throw new RuntimeException("Cannot update variable " + varname + ". Has not been declared.");
    }
    env.variables.put(varname, val);
  }

  /**
   * Get the value of a variable from the variable name
   * Throws a RuntimeException if the variable has not been declared
   */
  public RtVal getVar(String varname) {
    Scope env = getVarEnv(varname);
    if (env == null) {
      throw new RuntimeException("Tried to access " + varname + ", but it has not been declared");
    }
    return env.variables.get(varname);
  }

  /**
   * @returns the closest scope that contains a variable with the given name
   * @returns null if varname not found in any ancestor scope
   */
  public Scope getVarEnv(String varname) {
    Scope curr = this;
    while (curr != null) {
      if (curr.variables.containsKey(varname)) {
        return curr;
      }
      curr = curr.parent;
    }
    return null;
  }
}
