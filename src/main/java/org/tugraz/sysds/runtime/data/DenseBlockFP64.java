/*
 * Modifications Copyright 2018 Graz University of Technology
 * 
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


package org.tugraz.sysds.runtime.data;

import java.util.Arrays;

import org.tugraz.sysds.runtime.util.UtilFunctions;

public class DenseBlockFP64 extends DenseBlockDRB
{
	private static final long serialVersionUID = 8546723684649816489L;

	private double[] _data;

	public DenseBlockFP64(int[] dims) {
		super(dims);
		reset(_rlen, _odims, 0);
	}
	
	public DenseBlockFP64(int[] dims, double[] data) {
		super(dims);
		_data = data;
	}
	
	@Override
	public boolean isNumeric() {
		return true;
	}
	
	@Override
	public void reset(int rlen, int odims, double v) {
		int len = rlen * odims;
		if( len > capacity() ) {
			_data = new double[len];
			if( v != 0 )
				Arrays.fill(_data, v);
		}
		else {
			Arrays.fill(_data, 0, len, v);
		}
		_rlen = rlen;
		_odims = odims;
	}

	@Override
	public long capacity() {
		return (_data!=null) ? _data.length : -1;
	}

	@Override
	public long countNonZeros() {
		return UtilFunctions.computeNnz(_data, 0, _rlen*_odims);
	}
	
	@Override
	public int countNonZeros(int r) {
		return UtilFunctions.computeNnz(_data, r*_odims, _odims);
	}

	@Override
	public long countNonZeros(int rl, int ru, int ol, int ou) {
		long nnz = 0;
		if( ol == 0 && ou == _odims ) { //specific case: all cols
			nnz += UtilFunctions.computeNnz(_data, rl*_odims, (ru-rl)*_odims);
		}
		else {
			for( int i=rl, ix=rl*_odims; i<ru; i++, ix+=_odims )
				nnz += UtilFunctions.computeNnz(_data, ix+ol, ou-ol);
		}
		return nnz;
	}

	@Override
	public double[] values(int r) {
		return _data;
	}
	
	@Override
	public double[] valuesAt(int bix) {
		return _data;
	}

	@Override
	public int index(int r) {
		return 0;
	}

	@Override
	public int pos(int r) {
		return r * _odims;
	}

	@Override
	public int pos(int r, int c) {
		return r * _odims + c;
	}

	@Override
	public void incr(int r, int c) {
		_data[pos(r, c)] ++;
	}
	
	@Override
	public void incr(int r, int c, double delta) {
		_data[pos(r, c)] += delta;
	}
	
	@Override
	public DenseBlock set(double v) {
		Arrays.fill(_data, 0, _rlen*_odims, v);
		return this;
	}
	
	@Override
	public DenseBlock set(int rl, int ru, int ol, int ou, double v) {
		if( ol==0 && ou == _odims )
			Arrays.fill(_data, rl*_odims, ru*_odims, v);
		else
			for(int i=rl, ix=rl*_odims; i<ru; i++, ix+=_odims)
				Arrays.fill(_data, ix+ol, ix+ou, v);
		return this;
	}

	@Override
	public DenseBlock set(int r, int c, double v) {
		_data[pos(r, c)] = v;
		return this;
	}
	
	@Override
	public DenseBlock set(DenseBlock db) {
		System.arraycopy(db.valuesAt(0), 0, _data, 0, _rlen*_odims);
		return this;
	}
	
	@Override
	public DenseBlock set(int rl, int ru, int ol, int ou, DenseBlock db) {
		double[] a = db.valuesAt(0);
		if( ol == 0 && ou == _odims)
			System.arraycopy(a, 0, _data, rl*_odims+ol, (int)db.size());
		else {
			int len = ou - ol;
			for(int i=rl, ix1=0, ix2=rl*_odims+ol; i<ru; i++, ix1+=len, ix2+=_odims)
				System.arraycopy(a, ix1, _data, ix2, len);
		}
		return this;
	}

	@Override
	public DenseBlock set(int r, double[] v) {
		System.arraycopy(v, 0, _data, pos(r), _odims);
		return this;
	}

	@Override
	public double get(int r, int c) {
		return _data[pos(r, c)];
	}

	@Override
	public double get(int[] ix) {
		// TODO Auto-generated method stub
		return 0;
	}
}
