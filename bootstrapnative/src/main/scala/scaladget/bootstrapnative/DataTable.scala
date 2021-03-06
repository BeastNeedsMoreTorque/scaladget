package scaladget.bootstrapnative

import bsn._
import scaladget.tools._
import rx._
import scalatags.JsDom.all._
import scalatags.JsDom.tags
import scala.util.Try
import Table._


//NTable for Numerical table
object DataTable {

  case class DataRow(values: Seq[String], rowStyle: ModifierSeq = emptyMod, subRow: Option[SubRow] = None)

  case class Column(values: Seq[String])

  trait Sorting

  object NoSorting extends Sorting

  object PhantomSorting extends Sorting

  object AscSorting extends Sorting

  object DescSorting extends Sorting

  case class SortingStatus(col: Int, sorting: Sorting)

  def sortInt(seq: Seq[String]) = Try(
    seq.map {
      _.toInt
    }.zipWithIndex.sortBy {
      _._1
    }.map {
      _._2
    }
  ).toOption

  def sortDouble(seq: Seq[String]) = Try(
    seq.map {
      _.toDouble
    }.zipWithIndex.sortBy {
      _._1
    }.map {
      _._2
    }
  ).toOption

  def sortString(seq: Seq[String]): Seq[Int] = seq.zipWithIndex.sortBy {
    _._1
  }.map {
    _._2
  }

  def sort(s: Column): Seq[Int] = {
    sortInt(s.values) match {
      case Some(i: Seq[_]) => i
      case None => sortDouble(s.values) match {
        case Some(d: Seq[_]) => d
        case None => sortString(s.values)
      }
    }
  }

  def column(index: Int, rows: Seq[DataRow]): Column = Column(rows.map {
    _.values(index)
  })
}

import DataTable._

case class DataTable(headers: Option[Header] = None,
                     rows: Seq[DataTable.DataRow] = Seq(),
                     bsTableStyle: BSTableStyle = BSTableStyle(default_table, emptyMod),
                     sorting: Boolean = false) {

  val filteredRows = Var(rows)
  val selected: Var[Option[DataRow]] = Var(None)
  val nbColumns = rows.headOption.map {
    _.values.length
  }.getOrElse(0)

  val sortingStatuses = Var(
    Seq.fill(nbColumns)(
      if (sorting) SortingStatus(0, PhantomSorting) else SortingStatus(0, NoSorting))
  )


  def addHeaders(hs: String*) = copy(headers = Some(Header(hs)))

  def addRow(row: DataRow): DataTable = copy(rows = rows :+ row)

  def addRow(row: String*): DataTable = addRow(DataRow(row))

  def sortable = copy(sorting = true)


  private def fillRow(vals: Seq[String], rowType: RowType) = tags.tr(
    for (
      (cell, id) <- vals.zipWithIndex
    ) yield {
      rowType(cell, id)
    },
    backgroundColor := Rx {
      if (Some(row) == selected()) bsTableStyle.selectionColor else ""
    }
  )(onclick := { () =>
    selected() = Some(DataRow(vals))
  })

  def filter(containedString: String) = {
    filteredRows() = rows.filter { r =>
      r.values.mkString("").toUpperCase.contains(containedString.toUpperCase)
    }
  }

  def style(tableStyle: TableStyle = default_table, headerStyle: ModifierSeq = emptyMod) = {
    copy(bsTableStyle = BSTableStyle(tableStyle, headerStyle))
  }

  val sortingDiv = (n: Int) => tags.span(
    Rx {
      val ss = sortingStatuses()
      span(pointer, floatRight, lineHeight := "25px",
        ss(n).sorting match {
          case PhantomSorting => Seq(opacity := "0.4") +++ glyph_sort_by_attributes
          case AscSorting => glyph_sort_by_attributes
          case DescSorting => glyph_sort_by_attributes_alt
          case _ => emptyMod
        }
      )
    },
    onclick := { () =>
      if (sorting) {
        val ss = sortingStatuses.now
        sortingStatuses() = ss.map {
          _.copy(sorting = PhantomSorting)
        }.updated(n, ss(n).copy(sorting = ss(n).sorting match {
          case DescSorting | PhantomSorting => sort(n, AscSorting)
          case AscSorting => sort(n, DescSorting)
          case _ => PhantomSorting
        }
        )
        )
      }
    }
  )

  def sort(colIndex: Int, sorting: Sorting): Sorting = {
    val col = column(colIndex, filteredRows.now)
    val indexes: Seq[Int] = {
      val sorted = DataTable.sort(col)
      sorting match {
        case DescSorting => sorted.reverse
        case _ => sorted
      }
    }

    filteredRows() = {
      for (
        i <- indexes
      ) yield filteredRows.now(i)
    }
    sorting
  }

  val render = tags.table(bsTableStyle.tableStyle)(
    tags.thead(bsTableStyle.headerStyle)(
      headers.map { h => fillRow(h.values, (s: String, i: Int) => th(s, sortingDiv(i)))
      }),
    Rx {
      tags.tbody(
        for (r <- filteredRows()) yield {
          (Seq(Some(fillRow(r.values, (s: String, _) => td(s)))) :+
            r.subRow.map { sr =>
              tags.tr(r.rowStyle)(
                tags.td(colspan := nbColumns, padding := 0, borderTop := "0px solid black")(
                  sr.trigger.expand(sr.element())
                )
              )
            }).flatten
        }
      )
    }
  )
}

