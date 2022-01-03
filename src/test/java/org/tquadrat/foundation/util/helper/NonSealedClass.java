/*
 * ============================================================================
 *  Copyright © 2002-2021 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util.helper;

import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  A non-sealed class for testing purposes.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: NonSealedClass.java 858 2021-01-23 15:51:52Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: NonSealedClass.java 858 2021-01-23 15:51:52Z tquadrat $" )
public non-sealed class NonSealedClass extends SealedClass
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    @SuppressWarnings( {"javadoc", "RedundantNoArgConstructor"} )
    public NonSealedClass() { super(); }
}
//  class NonSealedClass

/*
 *  End of File
 */