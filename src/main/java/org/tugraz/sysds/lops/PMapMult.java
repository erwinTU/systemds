/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.tugraz.sysds.lops;

 
import org.tugraz.sysds.lops.LopProperties.ExecType;
import org.tugraz.sysds.runtime.instructions.InstructionUtils;
import org.tugraz.sysds.common.Types.DataType;
import org.tugraz.sysds.common.Types.ValueType;


public class PMapMult extends Lop 
{	
	public static final String OPCODE = "pmapmm";

	public PMapMult(Lop input1, Lop input2, DataType dt, ValueType vt) {
		super(Lop.Type.MapMult, dt, vt);
		this.addInput(input1);
		this.addInput(input2);
		input1.addOutput(this);
		input2.addOutput(this);
		lps.setProperties( inputs, ExecType.SPARK);
	}

	@Override
	public String toString() {
		return "Operation = PMapMM";
	}
	
	@Override
	public String getInstructions(String input1, String input2, String output) {
		return InstructionUtils.concatOperands(
			getExecType().name(),
			OPCODE,
			getInputs().get(0).prepInputOperand(input1),
			getInputs().get(1).prepInputOperand(input2),
			prepOutputOperand(output));
	}
}
