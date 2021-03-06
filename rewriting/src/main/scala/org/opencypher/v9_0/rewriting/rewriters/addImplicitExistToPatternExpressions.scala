/*
 * Copyright © 2002-2020 Neo4j Sweden AB (http://neo4j.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencypher.v9_0.rewriting.rewriters

import org.opencypher.v9_0.ast.semantics.SemanticState
import org.opencypher.v9_0.expressions.FunctionInvocation
import org.opencypher.v9_0.expressions.FunctionName
import org.opencypher.v9_0.expressions.PatternExpression
import org.opencypher.v9_0.expressions.functions.Exists
import org.opencypher.v9_0.util.Rewriter
import org.opencypher.v9_0.util.bottomUp
import org.opencypher.v9_0.util.symbols

/**
  * Adds an exist around any pattern expression that is expected to produce a boolean e.g.
  *
  *   MATCH (n) WHERE (n)-->(m) RETURN n
  *
  *    is rewritten to
  *
  *  MATCH (n) WHERE EXISTS((n)-->(m)) RETURN n
  *
  * This rewrite normalizes this cases and make it easier to plan correctly.
  */
case class addImplicitExistToPatternExpressions(semanticState: SemanticState) extends Rewriter {

  private val instance = bottomUp(Rewriter.lift {
    case p: PatternExpression if semanticState.expressionType(p).expected.contains(symbols.CTBoolean.invariant) =>
      FunctionInvocation(FunctionName(Exists.name)(p.position), p)(p.position)
  })

  override def apply(v: AnyRef): AnyRef = instance(v)
}
