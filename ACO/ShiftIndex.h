#ifndef SHIFTINDEX_H_
#define SHIFTINDEX_H_

#define EARLY	0
#define DAY		1
#define LATE	2
#define NIGHT	3
#define REST	4

inline int _j_d(int j) { return (j / 5); }
inline int _j_w(int j) { return ((j / 5) / 7); }

inline int _j_es(int j) { return 5 * (j / 5); }
inline int _j_ds(int j) { return 5 * (j / 5) + DAY; }
inline int _j_ls(int j) { return 5 * (j / 5) + LATE; }
inline int _j_ns(int j) { return 5 * (j / 5) + NIGHT; }
inline int _j_rs(int j) { return 5 * (j / 5) + REST; }

inline int _d_j(int d) { return 5 * d; }
inline int _w_j(int w) { return w * 5 * 7; }
inline int _d_w(int d) { return d / 7; }

inline int _es_j(int s) { return 5 * s; }
inline int _ds_j(int s) { return 5 * s + DAY; }
inline int _ls_j(int s) { return 5 * s + LATE; }
inline int _ns_j(int s) { return 5 * s + NIGHT; }
inline int _rs_j(int s) { return 5 * s + REST; }

inline int _sat_j(int sat) { return 5 * (7 * sat + 5); }

inline bool _working_day_d(int d) { int dw = d % 7; return (dw != 5 && dw != 6); }
inline bool _weekend_day_d(int d) { return !_working_day_d(d); }
inline bool _working_day_j(int j) { int dw = _j_d(j) % 7; return (dw != 5 && dw != 6); }
inline bool _weekend_day_j(int j) { return !_working_day_j(j); }
inline bool _night_shift_j(int j) { return (j % 5 == NIGHT); }

//inline int _ws_j(int ws) { return ws + (ws % 5 == 4) - (ws - 1) / 5; }

#endif /* SHIFTINDEX_H_ */