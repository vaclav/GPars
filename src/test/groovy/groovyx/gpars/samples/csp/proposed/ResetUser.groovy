// GPars (formerly GParallelizer)
//
// Copyright © 2008-9  The original author or authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package groovyx.gpars.samples.csp.proposed

//process ResetUser ( ! resetValue, ! toConsole, ? fromConverter, ! toClearOutput ) {
//
//	toConsole.write( "Please input reset values\n" )
//	while (true) {
//	  def v = fromConverter.read()
//	  toClearOutput.write("\n")
//	  resetValue.write(v)
//	}
//}