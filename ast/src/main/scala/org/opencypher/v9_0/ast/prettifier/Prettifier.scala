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
package org.opencypher.v9_0.ast.prettifier

import org.opencypher.v9_0.ast.ActionResource
import org.opencypher.v9_0.ast.AliasedReturnItem
import org.opencypher.v9_0.ast.AllGraphsScope
import org.opencypher.v9_0.ast.AllNodes
import org.opencypher.v9_0.ast.AllQualifier
import org.opencypher.v9_0.ast.AllRelationships
import org.opencypher.v9_0.ast.AllResource
import org.opencypher.v9_0.ast.AlterUser
import org.opencypher.v9_0.ast.AscSortItem
import org.opencypher.v9_0.ast.Clause
import org.opencypher.v9_0.ast.Create
import org.opencypher.v9_0.ast.CreateDatabase
import org.opencypher.v9_0.ast.CreateGraph
import org.opencypher.v9_0.ast.CreateIndex
import org.opencypher.v9_0.ast.CreateIndexNewSyntax
import org.opencypher.v9_0.ast.CreateNodeKeyConstraint
import org.opencypher.v9_0.ast.CreateNodePropertyExistenceConstraint
import org.opencypher.v9_0.ast.CreateRelationshipPropertyExistenceConstraint
import org.opencypher.v9_0.ast.CreateRole
import org.opencypher.v9_0.ast.CreateUniquePropertyConstraint
import org.opencypher.v9_0.ast.CreateUser
import org.opencypher.v9_0.ast.CreateView
import org.opencypher.v9_0.ast.DatabasePrivilege
import org.opencypher.v9_0.ast.DbmsPrivilege
import org.opencypher.v9_0.ast.DefaultDatabaseScope
import org.opencypher.v9_0.ast.Delete
import org.opencypher.v9_0.ast.DenyPrivilege
import org.opencypher.v9_0.ast.DescSortItem
import org.opencypher.v9_0.ast.DropConstraintOnName
import org.opencypher.v9_0.ast.DropDatabase
import org.opencypher.v9_0.ast.DropGraph
import org.opencypher.v9_0.ast.DropIndex
import org.opencypher.v9_0.ast.DropIndexOnName
import org.opencypher.v9_0.ast.DropNodeKeyConstraint
import org.opencypher.v9_0.ast.DropNodePropertyExistenceConstraint
import org.opencypher.v9_0.ast.DropRelationshipPropertyExistenceConstraint
import org.opencypher.v9_0.ast.DropRole
import org.opencypher.v9_0.ast.DropUniquePropertyConstraint
import org.opencypher.v9_0.ast.DropUser
import org.opencypher.v9_0.ast.DropView
import org.opencypher.v9_0.ast.ElementsAllQualifier
import org.opencypher.v9_0.ast.Foreach
import org.opencypher.v9_0.ast.FromGraph
import org.opencypher.v9_0.ast.GrantPrivilege
import org.opencypher.v9_0.ast.GrantRolesToUsers
import org.opencypher.v9_0.ast.GraphScope
import org.opencypher.v9_0.ast.IfExistsDoNothing
import org.opencypher.v9_0.ast.LabelAllQualifier
import org.opencypher.v9_0.ast.LabelQualifier
import org.opencypher.v9_0.ast.LabelsQualifier
import org.opencypher.v9_0.ast.Limit
import org.opencypher.v9_0.ast.LoadCSV
import org.opencypher.v9_0.ast.Match
import org.opencypher.v9_0.ast.Merge
import org.opencypher.v9_0.ast.MergeAction
import org.opencypher.v9_0.ast.NamedGraphScope
import org.opencypher.v9_0.ast.NodeByIds
import org.opencypher.v9_0.ast.NodeByParameter
import org.opencypher.v9_0.ast.OnCreate
import org.opencypher.v9_0.ast.OnMatch
import org.opencypher.v9_0.ast.OrderBy
import org.opencypher.v9_0.ast.PasswordString
import org.opencypher.v9_0.ast.PrivilegeQualifier
import org.opencypher.v9_0.ast.ProcedureResult
import org.opencypher.v9_0.ast.ProcedureResultItem
import org.opencypher.v9_0.ast.ProjectingUnionAll
import org.opencypher.v9_0.ast.ProjectingUnionDistinct
import org.opencypher.v9_0.ast.PropertiesResource
import org.opencypher.v9_0.ast.PropertyResource
import org.opencypher.v9_0.ast.Query
import org.opencypher.v9_0.ast.QueryPart
import org.opencypher.v9_0.ast.RelationshipAllQualifier
import org.opencypher.v9_0.ast.RelationshipByIds
import org.opencypher.v9_0.ast.RelationshipByParameter
import org.opencypher.v9_0.ast.RelationshipQualifier
import org.opencypher.v9_0.ast.RelationshipsQualifier
import org.opencypher.v9_0.ast.Remove
import org.opencypher.v9_0.ast.RemoveLabelItem
import org.opencypher.v9_0.ast.RemovePropertyItem
import org.opencypher.v9_0.ast.Return
import org.opencypher.v9_0.ast.ReturnItem
import org.opencypher.v9_0.ast.ReturnItemsDef
import org.opencypher.v9_0.ast.RevokePrivilege
import org.opencypher.v9_0.ast.RevokeRolesFromUsers
import org.opencypher.v9_0.ast.SeekOnly
import org.opencypher.v9_0.ast.SetClause
import org.opencypher.v9_0.ast.SetExactPropertiesFromMapItem
import org.opencypher.v9_0.ast.SetIncludingPropertiesFromMapItem
import org.opencypher.v9_0.ast.SetLabelItem
import org.opencypher.v9_0.ast.SetOwnPassword
import org.opencypher.v9_0.ast.SetPropertyItem
import org.opencypher.v9_0.ast.ShowAllPrivileges
import org.opencypher.v9_0.ast.ShowDatabase
import org.opencypher.v9_0.ast.ShowDatabases
import org.opencypher.v9_0.ast.ShowDefaultDatabase
import org.opencypher.v9_0.ast.ShowPrivilegeScope
import org.opencypher.v9_0.ast.ShowPrivileges
import org.opencypher.v9_0.ast.ShowRolePrivileges
import org.opencypher.v9_0.ast.ShowRoles
import org.opencypher.v9_0.ast.ShowUserPrivileges
import org.opencypher.v9_0.ast.ShowUsers
import org.opencypher.v9_0.ast.SingleQuery
import org.opencypher.v9_0.ast.Skip
import org.opencypher.v9_0.ast.Start
import org.opencypher.v9_0.ast.StartDatabase
import org.opencypher.v9_0.ast.Statement
import org.opencypher.v9_0.ast.StopDatabase
import org.opencypher.v9_0.ast.SubQuery
import org.opencypher.v9_0.ast.TraversePrivilege
import org.opencypher.v9_0.ast.UnaliasedReturnItem
import org.opencypher.v9_0.ast.Union
import org.opencypher.v9_0.ast.Union.UnionMapping
import org.opencypher.v9_0.ast.UnionAll
import org.opencypher.v9_0.ast.UnionDistinct
import org.opencypher.v9_0.ast.UnresolvedCall
import org.opencypher.v9_0.ast.Unwind
import org.opencypher.v9_0.ast.UseGraph
import org.opencypher.v9_0.ast.UserAllQualifier
import org.opencypher.v9_0.ast.UserQualifier
import org.opencypher.v9_0.ast.UsersQualifier
import org.opencypher.v9_0.ast.UsingHint
import org.opencypher.v9_0.ast.UsingIndexHint
import org.opencypher.v9_0.ast.UsingJoinHint
import org.opencypher.v9_0.ast.UsingScanHint
import org.opencypher.v9_0.ast.Where
import org.opencypher.v9_0.ast.With
import org.opencypher.v9_0.ast.WritePrivilege
import org.opencypher.v9_0.expressions.LabelName
import org.opencypher.v9_0.expressions.Parameter
import org.opencypher.v9_0.expressions.ParameterWithOldSyntax
import org.opencypher.v9_0.expressions.Property
import org.opencypher.v9_0.expressions.RelTypeName
import org.opencypher.v9_0.expressions.Variable

//noinspection DuplicatedCode
case class Prettifier(expr: ExpressionStringifier) {

  private val NL = System.lineSeparator()

  private val base = QueryPrettifier()

  def asString(statement: Statement): String = statement match {
    case q: Query =>
      base.query(q)

    case CreateIndex(LabelName(label), properties) =>
      s"CREATE INDEX ON :$label${properties.map(_.name).mkString("(", ", ", ")")}"

    case CreateIndexNewSyntax(variable, LabelName(label), properties, None) =>
      val propString = properties.map(p => s"${p.map.asInstanceOf[Variable].name}.${p.propertyKey.name}").mkString("(", ", ", ")")
      s"CREATE INDEX FOR (${variable.name}:$label) ON $propString"

    case CreateIndexNewSyntax(variable, LabelName(label), properties, Some(name)) =>
      val propString = properties.map(p => s"${p.map.asInstanceOf[Variable].name}.${p.propertyKey.name}").mkString("(", ", ", ")")
      s"CREATE INDEX ${Prettifier.escapeName(name)} FOR (${variable.name}:$label) ON $propString"

    case DropIndex(LabelName(label), properties) =>
      s"DROP INDEX ON :$label${properties.map(_.name).mkString("(", ", ", ")")}"

    case DropIndexOnName(name) =>
      s"DROP INDEX ${Prettifier.escapeName(name)}"

    case CreateNodeKeyConstraint(Variable(variable), LabelName(label), properties, None) =>
      s"CREATE CONSTRAINT ON ($variable:$label) ASSERT ${base.asString(properties)} IS NODE KEY"

    case CreateNodeKeyConstraint(Variable(variable), LabelName(label), properties, Some(name)) =>
      s"CREATE CONSTRAINT ${Prettifier.escapeName(name)} ON ($variable:$label) ASSERT ${base.asString(properties)} IS NODE KEY"

    case DropNodeKeyConstraint(Variable(variable), LabelName(label), properties) =>
      s"DROP CONSTRAINT ON ($variable:$label) ASSERT ${properties.map(_.asCanonicalStringVal).mkString("(", ", ", ")")} IS NODE KEY"

    case CreateUniquePropertyConstraint(Variable(variable), LabelName(label), properties, None) =>
      s"CREATE CONSTRAINT ON ($variable:$label) ASSERT ${properties.map(_.asCanonicalStringVal).mkString("(", ", ", ")")} IS UNIQUE"

    case CreateUniquePropertyConstraint(Variable(variable), LabelName(label), properties, Some(name)) =>
      s"CREATE CONSTRAINT ${Prettifier.escapeName(name)} ON ($variable:$label) ASSERT ${properties.map(_.asCanonicalStringVal).mkString("(", ", ", ")")} IS UNIQUE"

    case DropUniquePropertyConstraint(Variable(variable), LabelName(label), properties) =>
      s"DROP CONSTRAINT ON ($variable:$label) ASSERT ${properties.map(_.asCanonicalStringVal).mkString("(", ", ", ")")} IS UNIQUE"

    case CreateNodePropertyExistenceConstraint(Variable(variable), LabelName(label), property, None) =>
      s"CREATE CONSTRAINT ON ($variable:$label) ASSERT exists(${property.asCanonicalStringVal})"

    case CreateNodePropertyExistenceConstraint(Variable(variable), LabelName(label), property, Some(name)) =>
      s"CREATE CONSTRAINT ${Prettifier.escapeName(name)} ON ($variable:$label) ASSERT exists(${property.asCanonicalStringVal})"

    case DropNodePropertyExistenceConstraint(Variable(variable), LabelName(label), property) =>
      s"DROP CONSTRAINT ON ($variable:$label) ASSERT exists(${property.asCanonicalStringVal})"

    case CreateRelationshipPropertyExistenceConstraint(Variable(variable), RelTypeName(relType), property, None) =>
      s"CREATE CONSTRAINT ON ()-[$variable:$relType]-() ASSERT exists(${property.asCanonicalStringVal})"

    case CreateRelationshipPropertyExistenceConstraint(Variable(variable), RelTypeName(relType), property, Some(name)) =>
      s"CREATE CONSTRAINT ${Prettifier.escapeName(name)} ON ()-[$variable:$relType]-() ASSERT exists(${property.asCanonicalStringVal})"

    case DropRelationshipPropertyExistenceConstraint(Variable(variable), RelTypeName(relType), property) =>
      s"DROP CONSTRAINT ON ()-[$variable:$relType]-() ASSERT exists(${property.asCanonicalStringVal})"

    case DropConstraintOnName(name) =>
      s"DROP CONSTRAINT ${Prettifier.escapeName(name)}"

    case x: ShowUsers =>
      s"${x.name}"

    case x @ CreateUser(userName, initialPassword, requirePasswordChange, suspended, ifExistsDo) =>
      val userNameString = Prettifier.escapeName(userName)
      val ifNotExists = ifExistsDo match {
        case _: IfExistsDoNothing => " IF NOT EXISTS"
        case _                    => ""
      }
      val password = initialPassword match {
        case Left(_) => "'******'"
        case Right(param) => s"$$${param.name}"
      }
      val passwordString = s"SET PASSWORD $password CHANGE ${if (!requirePasswordChange) "NOT " else ""}REQUIRED"
      val statusString = if (suspended.isDefined) s" SET STATUS ${if (suspended.get) "SUSPENDED" else "ACTIVE"}"
      else ""
      s"${x.name} $userNameString$ifNotExists $passwordString$statusString"

    case x @ DropUser(userName, ifExists) =>
      if (ifExists) s"${x.name} ${Prettifier.escapeName(userName)} IF EXISTS"
      else s"${x.name} ${Prettifier.escapeName(userName)}"

    case x @ AlterUser(userName, initialPassword, requirePasswordChange, suspended) =>
      val userNameString = Prettifier.escapeName(userName)
      val passwordString = initialPassword match {
        case None => ""
        case Some(Left(_)) => s" '******'"
        case Some(Right(param)) => s" $$${param.name}"
      }
      val passwordModeString = if (requirePasswordChange.isDefined)
        s" CHANGE ${if (!requirePasswordChange.get) "NOT " else ""}REQUIRED"
      else
        ""
      val passwordPrefix = if (passwordString.nonEmpty || passwordModeString.nonEmpty) " SET PASSWORD" else ""
      val statusString = if (suspended.isDefined) s" SET STATUS ${if (suspended.get) "SUSPENDED" else "ACTIVE"}" else ""
      s"${x.name} $userNameString$passwordPrefix$passwordString$passwordModeString$statusString"

    case x @ SetOwnPassword(newPassword, currentPassword) =>
      def evalPassword(pw: Either[PasswordString, Parameter]): String = pw match {
        case Right(param) => s"$$${param.name}"
        case _ => s"'******'"
      }
      s"${x.name} FROM ${evalPassword(currentPassword)} TO ${evalPassword(newPassword)}"

    case x @ ShowRoles(withUsers, _) =>
      s"${x.name}${if (withUsers) " WITH USERS" else ""}"

    case x @ CreateRole(roleName, None, ifExistsDo) =>
      ifExistsDo match {
        case _: IfExistsDoNothing => s"${x.name} ${Prettifier.escapeName(roleName)} IF NOT EXISTS"
        case _                    => s"${x.name} ${Prettifier.escapeName(roleName)}"
      }

    case x @ CreateRole(roleName, Some(fromRole), ifExistsDo) =>
      ifExistsDo match {
        case _: IfExistsDoNothing => s"${x.name} ${Prettifier.escapeName(roleName)} IF NOT EXISTS AS COPY OF ${Prettifier.escapeName(fromRole)}"
        case _                    => s"${x.name} ${Prettifier.escapeName(roleName)} AS COPY OF ${Prettifier.escapeName(fromRole)}"
      }

    case x @ DropRole(roleName, ifExists) =>
      if (ifExists) s"${x.name} ${Prettifier.escapeName(roleName)} IF EXISTS"
      else s"${x.name} ${Prettifier.escapeName(roleName)}"

    case x @ GrantRolesToUsers(roleNames, userNames) if roleNames.length > 1 =>
      s"${x.name}S ${roleNames.map(Prettifier.escapeName).mkString(", ")} TO ${userNames.map(Prettifier.escapeName).mkString(", ")}"

    case x @ GrantRolesToUsers(roleNames, userNames) =>
      s"${x.name} ${roleNames.map(Prettifier.escapeName).mkString(", ")} TO ${userNames.map(Prettifier.escapeName).mkString(", ")}"

    case x @ RevokeRolesFromUsers(roleNames, userNames) if roleNames.length > 1 =>
      s"${x.name}S ${roleNames.map(Prettifier.escapeName).mkString(", ")} FROM ${userNames.map(Prettifier.escapeName).mkString(", ")}"

    case x @ RevokeRolesFromUsers(roleNames, userNames) =>
      s"${x.name} ${roleNames.map(Prettifier.escapeName).mkString(", ")} FROM ${userNames.map(Prettifier.escapeName).mkString(", ")}"

    case x @ GrantPrivilege(DbmsPrivilege(_), _, _, _, roleNames) =>
      s"${x.name} ON DBMS TO ${Prettifier.escapeNames(roleNames)}"

    case x @ DenyPrivilege(DbmsPrivilege(_), _, _, _, roleNames) =>
      s"${x.name} ON DBMS TO ${Prettifier.escapeNames(roleNames)}"

    case x @ RevokePrivilege(DbmsPrivilege(_), _, _, _, roleNames, _) =>
      s"${x.name} ON DBMS FROM ${Prettifier.escapeNames(roleNames)}"

    case x @ GrantPrivilege(DatabasePrivilege(_), _, dbScope, qualifier, roleNames) =>
      Prettifier.prettifyDatabasePrivilege(x.name, dbScope, qualifier, "TO", roleNames)

    case x @ DenyPrivilege(DatabasePrivilege(_), _, dbScope, qualifier, roleNames) =>
      Prettifier.prettifyDatabasePrivilege(x.name, dbScope, qualifier, "TO", roleNames)

    case x @ RevokePrivilege(DatabasePrivilege(_), _, dbScope, qualifier, roleNames, _) =>
      Prettifier.prettifyDatabasePrivilege(x.name, dbScope, qualifier, "FROM", roleNames)

    case x @ GrantPrivilege(TraversePrivilege(), _, dbScope, qualifier, roleNames) =>
      val (dbName, segment) = Prettifier.extractScope(dbScope, qualifier)
      s"${x.name} ON GRAPH $dbName $segment (*) TO ${Prettifier.escapeNames(roleNames)}"

    case x @ DenyPrivilege(TraversePrivilege(), _, dbScope, qualifier, roleNames) =>
      val (dbName, segment) = Prettifier.extractScope(dbScope, qualifier)
      s"${x.name} ON GRAPH $dbName $segment (*) TO ${Prettifier.escapeNames(roleNames)}"

    case x @ RevokePrivilege(TraversePrivilege(), _, dbScope, qualifier, roleNames, _) =>
      val (dbName, segment) = Prettifier.extractScope(dbScope, qualifier)
      s"${x.name} ON GRAPH $dbName $segment (*) FROM ${Prettifier.escapeNames(roleNames)}"

    case x @ GrantPrivilege(WritePrivilege(), _, dbScope, qualifier, roleNames) =>
      val (dbName, segment) = Prettifier.extractScope(dbScope, qualifier)
      s"${x.name} ON GRAPH $dbName $segment (*) TO ${Prettifier.escapeNames(roleNames)}"

    case x @ DenyPrivilege(WritePrivilege(), _, dbScope, qualifier, roleNames) =>
      val (dbName, segment) = Prettifier.extractScope(dbScope, qualifier)
      s"${x.name} ON GRAPH $dbName $segment (*) TO ${Prettifier.escapeNames(roleNames)}"

    case x @ RevokePrivilege(WritePrivilege(), _, dbScope, qualifier, roleNames, _) =>
      val (dbName, segment) = Prettifier.extractScope(dbScope, qualifier)
      s"${x.name} ON GRAPH $dbName $segment (*) FROM ${Prettifier.escapeNames(roleNames)}"

    case x @ GrantPrivilege(_, Some(resource), dbScope, qualifier, roleNames) =>
      val (resourceName, dbName, segment) = Prettifier.extractScope(resource, dbScope, qualifier)
      s"${x.name} {$resourceName} ON GRAPH $dbName $segment (*) TO ${Prettifier.escapeNames(roleNames)}"

    case x @ DenyPrivilege(_, Some(resource), dbScope, qualifier, roleNames) =>
      val (resourceName, dbName, segment) = Prettifier.extractScope(resource, dbScope, qualifier)
      s"${x.name} {$resourceName} ON GRAPH $dbName $segment (*) TO ${Prettifier.escapeNames(roleNames)}"

    case x @ RevokePrivilege(_, Some(resource), dbScope, qualifier, roleNames, _) =>
      val (resourceName, dbName, segment) = Prettifier.extractScope(resource, dbScope, qualifier)
      s"${x.name} {$resourceName} ON GRAPH $dbName $segment (*) FROM ${Prettifier.escapeNames(roleNames)}"

    case ShowPrivileges(scope) =>
      s"SHOW ${Prettifier.extractScope(scope)} PRIVILEGES"

    case x: ShowDatabases =>
      s"${x.name}"

    case x: ShowDefaultDatabase =>
      s"${x.name}"

    case x @ ShowDatabase(dbName) =>
      s"${x.name} ${Prettifier.escapeName(dbName)}"

    case x @ CreateDatabase(dbName, ifExistsDo) =>
      ifExistsDo match {
        case _: IfExistsDoNothing => s"${x.name} ${Prettifier.escapeName(dbName)} IF NOT EXISTS"
        case _                    => s"${x.name} ${Prettifier.escapeName(dbName)}"
      }

    case x @ DropDatabase(dbName, ifExists) =>
      if (ifExists) s"${x.name} ${Prettifier.escapeName(dbName)} IF EXISTS"
      else s"${x.name} ${Prettifier.escapeName(dbName)}"

    case x @ StartDatabase(dbName) =>
      s"${x.name} ${Prettifier.escapeName(dbName)}"

    case x @ StopDatabase(dbName) =>
      s"${x.name} ${Prettifier.escapeName(dbName)}"

    case x @ CreateGraph(catalogName, query) =>
      val graphName = catalogName.parts.mkString(".")
      s"${x.name} $graphName {$NL${base.indented().queryPart(query)}$NL}"

    case x @ DropGraph(catalogName) =>
      val graphName = catalogName.parts.mkString(".")
      s"${x.name} $graphName"

    case CreateView(catalogName, params, query, _) =>
      val graphName = catalogName.parts.mkString(".")
      val paramString = params.map(p => "$" + p.name).mkString("(", ", ", ")")
      s"CATALOG CREATE VIEW $graphName$paramString {$NL${base.indented().queryPart(query)}$NL}"

    case DropView(catalogName) =>
      val graphName = catalogName.parts.mkString(".")
      s"CATALOG DROP VIEW $graphName"
  }

  private case class QueryPrettifier(indentLevel: Int = 0) {
    def indented(): QueryPrettifier = copy(indentLevel + 1)

    private val INDENT = "  " * indentLevel

    private def asNewLine(l: String) = NL + l

    def query(q: Query): String = {
      val hint = q.periodicCommitHint.map(INDENT + "USING PERIODIC COMMIT" + _.size.map(" " + expr(_)).getOrElse("") + NL).getOrElse("")
      val query = queryPart(q.part)
      s"$hint$query"
    }

    def queryPart(part: QueryPart): String =
      part match {
        case SingleQuery(clauses) =>
          clauses.map(dispatch).mkString(NL)

        case union: Union =>
          val lhs = queryPart(union.part)
          val rhs = queryPart(union.query)
          val operation = union match {
            case _: UnionAll      => s"${INDENT}UNION ALL"
            case _: UnionDistinct => s"${INDENT}UNION"

            case u: ProjectingUnionAll      =>
              s"${INDENT}UNION ALL mappings: (${u.unionMappings.map(asString).mkString(", ")})"
            case u: ProjectingUnionDistinct =>
              s"${INDENT}UNION mappings: (${u.unionMappings.map(asString).mkString(", ")})"
          }
          Seq(lhs, operation, rhs).mkString(NL)

      }

    private def asString(u: UnionMapping): String = {
      s"${u.unionVariable.name}: [${u.variableInPart.name}, ${u.variableInQuery.name}]"
    }

    private def dispatch(clause: Clause) = clause match {
      case u: UseGraph       => asString(u)
      case f: FromGraph      => asString(f)
      case e: Return         => asString(e)
      case m: Match          => asString(m)
      case c: SubQuery       => asString(c)
      case w: With           => asString(w)
      case c: Create         => asString(c)
      case u: Unwind         => asString(u)
      case u: UnresolvedCall => asString(u)
      case s: SetClause      => asString(s)
      case r: Remove         => asString(r)
      case d: Delete         => asString(d)
      case m: Merge          => asString(m)
      case l: LoadCSV        => asString(l)
      case f: Foreach        => asString(f)
      case s: Start          => asString(s)
      case _                 => clause.asCanonicalStringVal // TODO
    }

    def asString(u: UseGraph): String =
      s"${INDENT}USE ${expr(u.expression)}"

    def asString(f: FromGraph): String =
      s"${INDENT}FROM ${expr(f.expression)}"

    def asString(m: Match): String = {
      val o = if (m.optional) "OPTIONAL " else ""
      val p = expr.patterns.apply(m.pattern)
      val ind = indented()
      val w = m.where.map(ind.asString).map(asNewLine).getOrElse("")
      val h = m.hints.map(ind.asString).map(asNewLine).mkString
      s"${INDENT}${o}MATCH $p$h$w"
    }

    def asString(c: SubQuery): String = {
      s"""${INDENT}CALL {
         |${indented().queryPart(c.part)}
         |${INDENT}}""".stripMargin
    }

    def asString(w: Where): String =
      s"${INDENT}WHERE ${expr(w.expression)}"

    def asString(m: UsingHint): String = {
      m match {
        case UsingIndexHint(v, l, ps, s) => Seq(
          s"${INDENT}USING INDEX ", if (s == SeekOnly) "SEEK " else "",
          expr(v), ":", expr(l),
          ps.map(expr(_)).mkString("(", ",", ")")
        ).mkString

        case UsingScanHint(v, l) => Seq(
          s"${INDENT}USING SCAN ", expr(v), ":", expr(l)
        ).mkString

        case UsingJoinHint(vs) => Seq(
          s"${INDENT}USING JOIN ON ", vs.map(expr(_)).toIterable.mkString(", ")
        ).mkString
      }
    }

    def asString(ma: MergeAction): String = ma match {
      case OnMatch(set)  => s"${INDENT}ON MATCH ${asString(set)}"
      case OnCreate(set) => s"${INDENT}ON CREATE ${asString(set)}"
    }

    def asString(m: Merge): String = {
      val p = expr.patterns.apply(m.pattern)
      val ind = indented()
      val a = m.actions.map(ind.asString).map(asNewLine).mkString
      s"${INDENT}MERGE $p$a"
    }

    def asString(o: Skip): String = s"${INDENT}SKIP ${expr(o.expression)}"
    def asString(o: Limit): String = s"${INDENT}LIMIT ${expr(o.expression)}"

    def asString(o: OrderBy): String = s"${INDENT}ORDER BY " + {
      o.sortItems.map {
        case AscSortItem(expression)  => expr(expression) + " ASCENDING"
        case DescSortItem(expression) => expr(expression) + " DESCENDING"
      }.mkString(", ")
    }

    def asString(r: ReturnItem): String = r match {
      case AliasedReturnItem(e, v)   => expr(e) + " AS " + expr(v)
      case UnaliasedReturnItem(e, _) => expr(e)
    }

    def asString(r: ReturnItemsDef): String = {
      val as = if (r.includeExisting) Seq("*") else Seq()
      val is = r.items.map(asString)
      (as ++ is).mkString(", ")
    }

    def asString(r: Return): String = {
      val d = if (r.distinct) " DISTINCT" else ""
      val i = asString(r.returnItems)
      val ind = indented()
      val o = r.orderBy.map(ind.asString).map(asNewLine).getOrElse("")
      val l = r.limit.map(ind.asString).map(asNewLine).getOrElse("")
      val s = r.skip.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}RETURN$d $i$o$s$l"
    }

    def asString(w: With): String = {
      val d = if (w.distinct) " DISTINCT" else ""
      val i = asString(w.returnItems)
      val ind = indented()
      val o = w.orderBy.map(ind.asString).map(asNewLine).getOrElse("")
      val l = w.limit.map(ind.asString).map(asNewLine).getOrElse("")
      val s = w.skip.map(ind.asString).map(asNewLine).getOrElse("")
      val wh = w.where.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}WITH$d $i$o$s$l$wh"
    }

    def asString(c: Create): String = {
      val p = expr.patterns.apply(c.pattern)
      s"${INDENT}CREATE $p"
    }

    def asString(u: Unwind): String = {
      s"${INDENT}UNWIND ${expr(u.expression)} AS ${expr(u.variable)}"
    }

    def asString(u: UnresolvedCall): String = {
      val namespace = expr(u.procedureNamespace)
      val prefix = if (namespace.isEmpty) "" else namespace + "."
      val arguments = u.declaredArguments.map(list => list.map(expr(_)).mkString("(", ", ", ")")).getOrElse("")
      val ind = indented()
      val yields = u.declaredResult.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}CALL $prefix${expr(u.procedureName)}$arguments$yields"
    }

    def asString(r: ProcedureResult): String = {
      def item(i: ProcedureResultItem) = i.output.map(expr(_) + " AS ").getOrElse("") + expr(i.variable)
      val items = r.items.map(item).mkString(", ")
      val ind = indented()
      val where = r.where.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}YIELD $items$where"
    }

    def asString(s: SetClause): String = {
      val items = s.items.map {
        case SetPropertyItem(prop, exp)                       => s"${expr(prop)} = ${expr(exp)}"
        case SetLabelItem(variable, labels)                   => expr(variable) + labels.map(l => s":${expr(l)}").mkString("")
        case SetIncludingPropertiesFromMapItem(variable, exp) => s"${expr(variable)} += ${expr(exp)}"
        case SetExactPropertiesFromMapItem(variable, exp)     => s"${expr(variable)} = ${expr(exp)}"
        case _                                                => s.asCanonicalStringVal
      }
      s"${INDENT}SET ${items.mkString(", ")}"
    }

    def asString(r: Remove): String = {
      val items = r.items.map {
        case RemovePropertyItem(prop)          => s"${expr(prop)}"
        case RemoveLabelItem(variable, labels) => expr(variable) + labels.map(l => s":${expr(l)}").mkString("")
        case _                                 => r.asCanonicalStringVal
      }
      s"${INDENT}REMOVE ${items.mkString(", ")}"
    }

    def asString(v: LoadCSV): String = {
      val withHeaders = if (v.withHeaders) " WITH HEADERS" else ""
      val url = expr(v.urlString)
      val varName = expr(v.variable)
      val fieldTerminator = v.fieldTerminator.map(x => " FIELDTERMINATOR " + expr(x)).getOrElse("")
      s"${INDENT}LOAD CSV$withHeaders FROM $url AS $varName$fieldTerminator"
    }

    def asString(delete: Delete): String = {
      val detach = if (delete.forced) "DETACH " else ""
      s"${INDENT}${detach}DELETE ${delete.expressions.map(expr(_)).mkString(", ")}"
    }

    def asString(foreach: Foreach): String = {
      val varName = expr(foreach.variable)
      val list = expr(foreach.expression)
      val updates = foreach.updates.map(dispatch).mkString(s"$NL  ", s"$NL  ", NL)
      s"${INDENT}FOREACH ( $varName IN $list |$updates)"
    }

    def asString(start: Start): String = {
      val startItems =
        start.items.map {
          case AllNodes(v)                                               => s"${expr(v)} = NODE( * )"
          case NodeByIds(v, ids)                                         => s"${expr(v)} = NODE( ${ids.map(expr(_)).mkString(", ")} )"
          case NodeByParameter(v, param: Parameter)                      => s"${expr(v)} = NODE( ${expr(param)} )"
          case NodeByParameter(v, param: ParameterWithOldSyntax)         => s"${expr(v)} = NODE( ${expr(param)} )"
          case AllRelationships(v)                                       => s"${expr(v)} = RELATIONSHIP( * )"
          case RelationshipByIds(v, ids)                                 => s"${expr(v)} = RELATIONSHIP( ${ids.map(expr(_)).mkString(", ")} )"
          case RelationshipByParameter(v, param: Parameter)              => s"${expr(v)} = RELATIONSHIP( ${expr(param)} )"
          case RelationshipByParameter(v, param: ParameterWithOldSyntax) => s"${expr(v)} = RELATIONSHIP( ${expr(param)} )"
        }

      val ind = indented()
      val where = start.where.map(ind.asString).map(asNewLine).getOrElse("")
      s"${INDENT}START ${startItems.mkString(s",$NL      ")}$where"
    }

    def asString(properties: Seq[Property]): String =
      properties.map(_.asCanonicalStringVal).mkString("(", ", ", ")")
  }
}

object Prettifier {

  def extractScope(scope: ShowPrivilegeScope): String = {
    scope match {
      case ShowUserPrivileges(name) =>
        if(name.isDefined)
          s"USER ${escapeName(name.get)}"
        else
          "USER"
      case ShowRolePrivileges(name) => s"ROLE ${escapeName(name)}"
      case ShowAllPrivileges()      => "ALL"
      case _                        => "<unknown>"
    }
  }

  def extractScope(dbScope: GraphScope, qualifier: PrivilegeQualifier): (String, String) = (extractDbScope(dbScope)._1, extractQualifierPart(qualifier))

  def extractScope(resource: ActionResource, dbScope: GraphScope, qualifier: PrivilegeQualifier): (String, String, String) = {
    val resourceName = resource match {
      case PropertyResource(name)    => escapeName(name)
      case PropertiesResource(names) => names.map(escapeName).mkString(", ")
      case AllResource()             => "*"
      case _                         => "<unknown>"
    }
    (resourceName, extractDbScope(dbScope)._1, extractQualifierPart(qualifier))
  }

  def revokeOperation(operation: String, revokeType: String) = s"$operation($revokeType)"

  def prettifyDatabasePrivilege(privilegeName: String,
                                        dbScope: GraphScope,
                                        qualifier: PrivilegeQualifier,
                                        preposition: String,
                                        roleNames: Seq[Either[String, Parameter]]): String = {
    val (dbName, default) = Prettifier.extractDbScope(dbScope)
    val db = if (default) s"DEFAULT DATABASE" else s"DATABASE $dbName"
    qualifier match {
      case _: AllQualifier =>
        s"$privilegeName ON $db $preposition ${Prettifier.escapeNames(roleNames)}"
      case _ =>
        val qualifierString = Prettifier.extractQualifierPart(qualifier)
        s"$privilegeName $qualifierString ON $db $preposition ${Prettifier.escapeNames(roleNames)}"
    }
  }

  def extractQualifierPart(qualifier: PrivilegeQualifier): String = qualifier match {
    case LabelQualifier(name)          => "NODE " + escapeName(name)
    case LabelsQualifier(names)        => "NODES " + names.map(escapeName).mkString(", ")
    case LabelAllQualifier()           => "NODES *"
    case RelationshipQualifier(name)   => "RELATIONSHIP " + escapeName(name)
    case RelationshipsQualifier(names) => "RELATIONSHIPS " + names.map(escapeName).mkString(", ")
    case RelationshipAllQualifier()    => "RELATIONSHIPS *"
    case ElementsAllQualifier()        => "ELEMENTS *"
    case UsersQualifier(names)         => "(" + names.map(escapeName).mkString(", ") + ")"
    case UserQualifier(name)           => "(" + escapeName(name) + ")"
    case UserAllQualifier()            => "(*)"
    case AllQualifier()                => "*"
    case _                             => "<unknown>"
  }

  def extractDbScope(dbScope: GraphScope): (String, Boolean) = dbScope match {
    case NamedGraphScope(name)  => (escapeName(name), false)
    case AllGraphsScope()       => ("*", false)
    case DefaultDatabaseScope() => ("DEFAULT", true)
    case _                      => ("<unknown>", false)
  }

  /*
   * Some strings (identifiers) were escaped with back-ticks to allow non-identifier characters
   * When printing these again, the knowledge of the back-ticks is lost, but the same test for
   * non-identifier characters can be used to recover that knowledge.
   */
  def escapeName(name: String): String = {
    if (name.isEmpty)
      name
    else {
      val c = name.chars().toArray.toSeq
      if (Character.isJavaIdentifierStart(c.head) && Character.getType(c.head) != Character.CURRENCY_SYMBOL &&
        (c.tail.isEmpty || c.tail.forall(Character.isJavaIdentifierPart)))
        name
      else
        s"`$name`"
    }
  }

  def escapeName(name: Either[String, Parameter]): String = name match {
    case Left(s) => escapeName(s)
    case Right(p) => s"$$${p.name}"
  }

  def escapeNames(names: Seq[Either[String, Parameter]]): String = names.map(escapeName).mkString(", ")

}
