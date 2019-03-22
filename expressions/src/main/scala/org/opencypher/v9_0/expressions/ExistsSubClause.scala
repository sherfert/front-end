/*
 * Copyright © 2002-2019 Neo4j Sweden AB (http://neo4j.com)
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
package org.opencypher.v9_0.expressions

import org.opencypher.v9_0.util.InputPosition

case class ExistsSubClause(pattern: Pattern, optionalWhereExpression: Option[Expression])
                          (val position: InputPosition, val outerScope: Set[LogicalVariable]) extends ScopeExpression {

  self =>

  def withOuterScope(outerScope: Set[LogicalVariable]): ExistsSubClause =
    copy()(position, outerScope)

  override val introducedVariables: Set[LogicalVariable] = pattern.patternParts.head.element.allVariables -- outerScope

  override def dup(children: Seq[AnyRef]): this.type = {
    ExistsSubClause(
      children(0).asInstanceOf[Pattern],
      children(1).asInstanceOf[Option[Expression]],
    )(position, outerScope).asInstanceOf[this.type]
  }
}