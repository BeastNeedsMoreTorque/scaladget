package fr.iscpif.demo

import org.scalajs.dom._

/*
 * Copyright (C) 24/08/16 // mathieu.leclaire@openmole.org
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

object LabelDemo {
  val sc = sourcecode.Text {
    import fr.iscpif.scaladget.stylesheet.{all => sheet}
    import fr.iscpif.scaladget.api.{BootstrapTags => bs}
    import fr.iscpif.scaladget.tools.JsRxTags._
    import scalatags.JsDom.all._
    import sheet._
    import rx._

    val hovered = Var("None")
    val buttonStyle: ModifierSeq = Seq(
      sheet.marginRight(5)
    )

    def overAction(tag: String) = onmouseover := { () =>  hovered() = tag }

    div(
      label("Default", buttonStyle +++ label_default, overAction("default")),
      label("Primary", buttonStyle +++ label_primary, overAction("primary")),
      label("Info", buttonStyle +++ label_info,  overAction("info")),
      label("Success", buttonStyle +++ label_success, overAction("success")),
      label("Warning", buttonStyle +++ label_warning, overAction("warning")),
      label("Danger", buttonStyle +++ label_danger, overAction("danger")),
      Rx{
        div(sheet.paddingTop(15), s"Hovered: ${hovered()}")
      }
    ).render
  }

  val elementDemo = new ElementDemo {
    def title: String = "Label"

    def code: String = sc.source

    def element: Element = sc.value
  }
}