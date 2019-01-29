/*
 * Copyright © 2002-2018 Neo4j Sweden AB (http://neo4j.com)
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
package org.opencypher.v9_0.rewriting

import org.opencypher.v9_0.ast.AstConstructionTestSupport
import org.opencypher.v9_0.expressions._
import org.opencypher.v9_0.rewriting.rewriters.normalizeComparisons
import org.opencypher.v9_0.util.test_helpers.CypherFunSuite
import org.opencypher.v9_0.expressions._

class NormalizeComparisonsTest extends CypherFunSuite with AstConstructionTestSupport {

  val expression: Expression = Variable("foo")(pos)
  val comparisons = List(
    Equals(expression, expression)(pos),
    NotEquals(expression, expression)(pos),
    LessThan(expression, expression)(pos),
    LessThanOrEqual(expression, expression)(pos),
    GreaterThan(expression, expression)(pos),
    GreaterThanOrEqual(expression, expression)(pos),
    InvalidNotEquals(expression, expression)(pos)
  )

  comparisons.foreach { operator =>
    test(operator.toString) {
      val rewritten = operator.endoRewrite(normalizeComparisons)

      rewritten.lhs shouldNot be theSameInstanceAs rewritten.rhs
    }
  }

  test("extract multiple hasLabels") {
    val original = HasLabels(varFor("a"), Seq(labelName("X"), labelName("Y")))(pos)

    original.endoRewrite(normalizeComparisons) should equal(
      Ands(Set(
        HasLabels(varFor("a"), Seq(labelName("X")))(pos),
        HasLabels(varFor("a"), Seq(labelName("Y")))(pos)))(pos))
  }

  test("does not extract single hasLabels") {
    val original = HasLabels(varFor("a"), Seq(labelName("Y")))(pos)

    original.endoRewrite(normalizeComparisons) should equal(original)
  }
}
