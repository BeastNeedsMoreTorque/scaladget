package fr.iscpif.demo


import fr.iscpif.scaladget.api.DropDown
import fr.iscpif.scaladget.stylesheet.all
import org.scalajs.dom.Element

/*
 * Copyright (C) 22/08/16 // mathieu.leclaire@openmole.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import fr.iscpif.scaladget.api.{BootstrapTags => bs}
import fr.iscpif.scaladget.stylesheet.{all => sheet}
import scalatags.JsDom.all._
import sheet._
import bs._

object SelectDemo {

  val sc = sourcecode.Text {
    import fr.iscpif.scaladget.api.DropDown._

    // Define a toy case class containing at least a name attribute
    case class MyElement(name: String)

    // Define the option sequence
    val elements = Seq(
      MyElement("First element"),
      MyElement("Second Element"),
      MyElement("Third Element")
    )

    val optionDropDown =
      elements.map { e =>
        option(e, e.name)
      }.dropdown(1, btn_success).selector

    val loginInput = bs.input("")(placeholder := "Login")
    val passInput = bs.input("")(placeholder := "Login", `type` := "password")

    val formDropDown =
      bs.vForm(width := 200)(
        loginInput.withLabel("Login"),
        passInput.withLabel("Pass"),
        bs.button("OK", btn_primary, ()=> println("OK"))
      ).dropdown("Form", btn_primary +++ sheet.marginLeft(10), () => println("Dropdown closed"))


    div(optionDropDown, formDropDown).render

  }


  val elementDemo = new ElementDemo {
    def title: String = "Dropdown"

    def code: String = sc.source

    def element: Element = sc.value
  }

}
