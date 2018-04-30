package demo


import org.scalajs.dom.Element
import org.scalajs.dom.raw.{Event, HTMLButtonElement}
import scaladget.ace.ace
import scaladget.bootstrapnative.bsn
import scaladget.bootstrapnative.bsn._
import scalatags.JsDom.all._


object PlayGroundDemo {
  val sc = sourcecode.Text {
    import rx._

    val table = bsn.table.
      addHeaders("Title 1", "Title 2", "Title 3").
      addRow("0.1", "158", "3").
      addRow("0.006", "bb", "236").
      addRow("21", "zz", "302").
      addRow("151", "a", "33")

    val table2 = bsn.table.
      addHeaders("Title 10", "Title 20", "Title 30").
      addRow("0.1", "158", "3").
      addRow("0.006", "bb", "236").
      addRow("21", "zz", "302").
      addRow("151", "a", "33")

    val filteredTable = table.style(bordered_table).sortable

    lazy val filterInput = inputTag("")(marginBottom := 20).render
    filterInput.oninput = (e: Event) ⇒ {
      filteredTable.filter(filterInput.value)
    }

    val tableRender = filteredTable.render
    val tableRender2 = table2.render


    //Editor 1
    val editorDiv = div(id := "editor", height := 100, paddingRight := 20).render
    val editor = ace.edit(editorDiv)
    val session = editor.getSession()

    session.setValue("val a = 7")
    session.setMode("ace/mode/scala")
    editor.setTheme("ace/theme/github")

    val editorRender = div(editorDiv)


    //Editor 2
    val editorDiv2 = div(id := "editor", height := 100, paddingRight := 20).render
    val editor2 = ace.edit(editorDiv2)
    val session2 = editor2.getSession()

    session2.setValue("val a = 777")
    session2.setMode("ace/mode/scala")
    editor2.setTheme("ace/theme/github")

    val editorRender2 = div(editorDiv2)


    lazy val tabs2: Tabs = Tabs.tabs().
      add("Raw", editorRender).
      add("Table", tableRender).build

    val switch: Var[Boolean] = Var(false)


    lazy val switchButton: HTMLButtonElement = button("Switch", btn_default, onclick := { () =>
      val currentTitle = theTabs.active.now.map {
        _.title
      }.getOrElse("NEW")


      val index = theTabs.active.now.map { i => theTabs.tabs.now.indexOf(i) }

      theTabs.active.now.map { t =>
        theTabs.remove(t)
      }

      val newTab = Tab(currentTitle,
        div(
          switchButton,
          if (switch.now) tableRender else editorRender
        ),
        () => {})

      index.map { i =>
        val tabsnow = theTabs.tabs.now
        theTabs.tabs() = tabsnow.take(i) ++ Seq(newTab) ++ tabsnow.takeRight(tabsnow.size - i)
      }

      switch() = !switch.now
      theTabs.setActive(newTab)
    }).render


    lazy val theTabs = Tabs.tabs().closable.
      add("Table", tableRender2).
      add("Div", div("yo")).
      add("Etitor", editorRender2).
      add("Switch",
        div(
          switchButton,
          tableRender
        )).build

    div(
      h3("TABS"),
      theTabs.render
    ).render


  }

  val elementDemo = new ElementDemo {
    def title: String = "PlayGround"

    def code: String = sc.source

    def element: Element = sc.value
  }
}