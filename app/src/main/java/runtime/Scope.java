/**
 * Scope.java
 *
 * Stores the variables in the current scope, and allows variables in parent/ancestor scopes to be accessed/updated.
 */

package runtime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Scope {

  Scope parent;
  Map<String, RtVal> variables; // stores <varname, value> pairs
  Set<String> constants;

  public static Scope createGlobalScope() {
    Scope s = new Scope(null);
    s.declareVar("null", new RtNull(), true);
    s.declareVar("true", new RtBool(true), true);
    s.declareVar("false", new RtBool(false), true);
    return s;
  }

  public Scope(Scope parent) {
    this.parent = parent;
    this.variables = new HashMap<>();
    this.constants = new HashSet<>();
  }

  /**
   * Declare a new variable with a given value.
   * Throws a RuntimeError if the variable name is already used in this scope.
   */
  public void declareVar(String varname, RtVal val, boolean isConst) {
    if (variables.containsKey(varname)) {
      throw new RuntimeException(
          "Cannot declare variable " + varname
              + ". A variable with the same name already exists in this scope.");
    }
    if (isConst)
      constants.add(varname);
    variables.put(varname, val);
  }

  /**
   * Update the value of a variable if it has already been declared.
   * Throws a RuntimeError if the variable has not yet been declared, or if it was
   * declared as constant.
   */
  public RtVal assignVar(String varname, RtVal val) {
    Scope env = getVarEnv(varname);
    if (env == null) {
      throw new RuntimeException("Cannot update variable " + varname + ". Has not been declared.");
    }
    if (constants.contains(varname)) {
      throw new RuntimeException("Cannot update variable " + varname + ". Declared as constant");
    }
    env.variables.put(varname, val);
    return val;
  }

  public RtVal assignVar(RtObject object, String key, RtVal val) {
    object.props.put(key, val);
    return val;
  }

  /**
   * Get the value of a variable from the variable name
   * Throws a RuntimeException if the variable has not been declared
   */
  public RtVal getVar(String varname) {
    Scope env = getVarEnv(varname);
    return env.variables.get(varname);
  }

  public RtVal getVar(RtObject object, String key) {
    if (object.props.containsKey(key))
      return object.props.get(key);
    else
      throw new RuntimeException("Key '" + key + "' does not exist in object:\n" + object);
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
    throw new RuntimeException("Cannot resolve " + varname + " as it doesn't exist.");
  }
}
