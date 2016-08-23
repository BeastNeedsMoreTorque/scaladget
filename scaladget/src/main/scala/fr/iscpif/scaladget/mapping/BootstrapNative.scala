package fr.iscpif.scaladget.mapping

/*
 * Copyright (C) 18/08/16 // mathieu.leclaire@openmole.org
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


import org.scalajs.dom.Element

import scala.scalajs.js
import js.annotation._


//@js.native
/*trait BootstrapNative extends js.Object{
  def modal(element: Element): Modal = js.native
}*/


//@js.native
//@JSName("bootstrapnative")
//object bootstrapnative extends BootstrapNative


/* @js.native
 trait ModalOptions extends js.Object {

 }*/

@js.native
class Modal(element: Element) extends js.Object {
  println("Modal constr")
  def close: js.Dynamic = js.native
}