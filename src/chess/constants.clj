(ns chess.constants)

(def THRESHOLD [0 7])

(def ALL-DISPLACEMENTS [[0 1]
                        [0 -1]
                        [-1 0]
                        [1 0]
                        [1 1]
                        [1 -1]
                        [-1 1]
                        [-1 -1]])

(def ROOK-DISPLACEMENTS [[0 1]
                         [0 -1]
                         [-1 0]
                         [1 0]])

(def BLACK-PAWN-DISPLACEMENTS [[-1 -1]
                               [1 -1]
                               [0 -1]])

(def WHITE-PAWN-DISPLACEMENTS [[1 1]
                               [-1 1]
                               [0 1]])

(def BISHOP-DISPLACEMENTS [[1 1]
                           [1 -1]
                           [-1 1]
                           [-1 -1]])

(def KNIGHT-DISPLACEMENTS [[2 1]
                           [2 -1]
                           [-2 1]
                           [-2 -1]
                           [1 2]
                           [1 -2]
                           [-1 2]
                           [-1 -2]])