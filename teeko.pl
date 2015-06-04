%%%%%%%%%%%%%%%%%%%%%
%%      TEEKO      %%
%%%%%%%%%%%%%%%%%%%%%

:- dynamic position/3.

%Check that A is different from B and C is different from D simultaneously
sdif(A,B,_,_) :- dif(A,B), !.
sdif(_,_,C,D) :- dif(C,D), !.

%Return all the positions of player's markers
positions('B', X1, Y1, X2, Y2, X3, Y3, X4, Y4) :- position(X1, Y1, 'B1'), position(X2, Y2, 'B2'), position(X3, Y3, 'B3'), position(X4, Y4, 'B4'), !.
positions('R', X1, Y1, X2, Y2, X3, Y3, X4, Y4) :- position(X1, Y1, 'R1'), position(X2, Y2, 'R2'), position(X3, Y3, 'R3'), position(X4, Y4, 'R4'), !.

%Check that all elements of first argument list are between MIN and MAX
inRange([T|R], MIN, MAX) :- between(MIN, MAX, T), inRange(R, MIN, MAX).
inRange([], _, _).

%Return all markers name depending on color
markers('B', ['B1', 'B2', 'B3', 'B4']).
markers('R', ['R1', 'R2', 'R3', 'R4']).

%Return oppositionist name
flipPlayer('R', 'B').
flipPlayer('B', 'R').

%Return all the first element of all element of first argument list
extractScore([], []) :- !.
extractScore([[T|_]|R2], [T|R3]) :- extractScore(R2, R3).

%Return min or max of the list depending on first argument
min_or_max(1, L, V) :- max_list(L, V).
min_or_max(-1, L, V) :- min_list(L, V).

%Last unset marker
last_marker(Player, Marker) :- markers(Player, MarkersList), not_isset(MarkersList, Marker).

%Return first unset Marker
not_isset([M|_], M) :- not(position(_,_,M)), !.
not_isset([T|R], M) :- position(_,_,T), not_isset(R, M).

%Check that B player have win
winner('B') :-
    position(B1X, B1Y, 'B1'),
    position(B2X, B2Y, 'B2'),
    position(B3X, B3Y, 'B3'),
    position(B4X, B4Y, 'B4'),
    winner(B1X, B1Y, B2X, B2Y, B3X, B3Y, B4X, B4Y), !.

%Check that R player have win
winner('R') :-
    position(R1X, R1Y, 'R1'),
    position(R2X, R2Y, 'R2'),
    position(R3X, R3Y, 'R3'),
    position(R4X, R4Y, 'R4'),
    winner(R1X, R1Y, R2X, R2Y, R3X, R3Y, R4X, R4Y), !.


%Detect line
winner(M1X, M1Y, M2X, M2Y, M3X, M3Y, M4X, M4Y) :- 
    permutation([[X1, Y], [X2, Y], [X3, Y], [X4, Y]], [[M1X, M1Y], [M2X, M2Y], [M3X, M3Y], [M4X, M4Y]]),
    inRange([X1, X2, X3, X4, Y], 1, 5),
    X2 is X1 + 1, X3 is X2 + 1, X4 is X3 + 1.

%Detect colon
winner(M1X, M1Y, M2X, M2Y, M3X, M3Y, M4X, M4Y) :-
    permutation([[X, Y1], [X, Y2], [X, Y3], [X, Y4]], [[M1X, M1Y], [M2X, M2Y], [M3X, M3Y], [M4X, M4Y]]),
    inRange([X, Y1, Y2, Y3, Y4], 1, 5),
    Y2 is Y1 + 1, Y3 is Y2 + 1, Y4 is Y3 + 1.

%Detect diagonal
winner(M1X, M1Y, M2X, M2Y, M3X, M3Y, M4X, M4Y) :-
    permutation([[X1, Y1], [X2, Y2], [X3, Y3], [X4, Y4]], [[M1X, M1Y], [M2X, M2Y], [M3X, M3Y], [M4X, M4Y]]),
    inRange([X1, X2, X3, X4, Y1, Y2, Y3, Y3], 1, 5),
    Y2 is Y1 + 1, X2 is X1 + 1, Y3 is Y2 + 1, X3 is X2 + 1, Y4 is Y3 + 1, X4 is X3 + 1.
    
%Detect otherDiagonal
winner(M1X, M1Y, M2X, M2Y, M3X, M3Y, M4X, M4Y) :-
    permutation([[X1, Y1], [X2, Y2], [X3, Y3], [X4, Y4]], [[M1X, M1Y], [M2X, M2Y], [M3X, M3Y], [M4X, M4Y]]),
    inRange([X1, X2, X3, X4, Y1, Y2, Y3, Y3], 1, 5),
    Y2 is Y1 + 1, X2 is X1 - 1, Y3 is Y2 + 1, X3 is X2 - 1, Y4 is Y3 + 1, X4 is X3 - 1.
    
%Detect square
winner(M1X, M1Y, M2X, M2Y, M3X, M3Y, M4X, M4Y) :-
    permutation([[X1, Y1], [X2, Y1], [X2, Y2], [X1, Y2]], [[M1X, M1Y], [M2X, M2Y], [M3X, M3Y], [M4X, M4Y]]),
    inRange([X1, X2, Y1, Y2], 1, 5),
    X2 is X1 + 1, Y2 is Y1 + 1.

%calcSet for M
calcSet(X, Y, M) :-
    inRange([X, Y], 1, 5),
    not(position(X, Y, _)),
    not(position(_, _, M)).

%Set marker on board
set(X, Y, M) :- nonvar(X), nonvar(Y), nonvar(M), calcSet(X, Y, M), assert(position(X, Y, M)), !.
set(PosX, PosY, X, Y, M) :- nonvar(X), nonvar(Y), nonvar(M), calcSet(X, Y, M), assert(position(X, Y, M)), PosX = X, PosY = Y, !.

%Unset marker off board
unset(M) :- nonvar(M), position(X, Y, M), retract(position(X, Y, M)), !.

%calcAllSet : calculate all positions available to set a marker and check if the marker is the first which can be set
calcAllSet(C, M, X, Y) :-
    last_marker(C, M),
    calcSet(X, Y, 'NE').

%move
move(TOX, TOY, M) :- position(FROMX, FROMY, M), calcMove(TOX, TOY, M), assert(position(TOX, TOY, M)), retract(position(FROMX, FROMY, M)), !.
move(PosX, PosY, PosXO, PosYO, TOX, TOY, M) :- position(FROMX, FROMY, M), calcMove(TOX, TOY, M), assert(position(TOX, TOY, M)), retract(position(FROMX, FROMY, M)), PosX is TOX, PosY is TOY, PosXO is FROMX, PosYO is FROMY, !.

%calcAllMove : calculate all moves available for all markers of a color
calcAllMove(C, M, TOX, TOY) :-
    markers(C, LM),
    member(M, LM),
    calcMove(TOX, TOY, M).

%calcMove for M
calcMove(TOX, TOY, M) :-
    position(FROMX, FROMY, M),
    inRange([TOX, TOY], 1, 5),
    MAXX is FROMX + 1, MINX is FROMX - 1, inRange([TOX], MINX, MAXX),
    MAXY is FROMY + 1, MINY is FROMY - 1, inRange([TOY], MINY, MAXY),
    sdif(TOX, FROMX, TOY, FROMY),
    not(position(TOX, TOY, _)).

%calcAll : calculate all moves set depending on phase
calcAll(C, M, X, Y) :- last_marker(C, M), calcAllSet(C, M, X, Y).
calcAll(C, M, X, Y) :- not(last_marker(C, M)), calcAllMove(C, M, X, Y).

%Top-level predicate for IA algorithm
bestChange(C, _, _, M,3,3):- nbSetMarkers(C,0, _), not(position(3,3,_)), last_marker(C, M),!.
bestChange(C, _, _, M,X,Y):- nbSetMarkers(C,0, _), last_marker(C, M), random(RndX), random(RndY), X is 2 + 2 * round(RndX), Y is 2 + 2 * round(RndY),!.
bestChange(C, L, IAL, M, X, Y) :- bestChange(C, L, IAL, _, 1, -200, 200, [M, X, Y]).

%detect player win change depending on difficulty
detectPlayerWinChange(3).

%detect opponent win change depending on difficulty
detectOpponentWinChange(3) :- random(X), X < 0.8.

%bestChange : return the best change (move or set for a player)
bestChange(C, 0, L, X, _, _, _, _) :- flipPlayer(C, C1), eval(L, C1, X).
bestChange(C, _, L, X, MINMAX, _, _, _) :- winner(C), detectOpponentWinChange(L), X is MINMAX*100.
bestChange(C, _, L, X, MINMAX, _, _, _) :- flipPlayer(C, C2), winner(C2), detectPlayerWinChange(L), X is -1*MINMAX*90.
bestChange(C, V, IAL, RE, MINMAX, ALPHA, BETA, ADD) :- dif(V, 0), findall([C, M, X, Y], calcAll(C, M, X, Y), L), VAL is -1 * MINMAX * 200, change(L, V, IAL, LR, MINMAX, ALPHA, BETA, VAL), extractScore(LR, LV), min_or_max(MINMAX, LV, RE), nth1(_, LR, [RE|ADD]), !.

%Main part of the algorithm
%Depending on phase of game (first or second)
change([[C, M, X, Y]|R], V, IAL, [TE|RE], MINMAX, ALPHA, BETA, VAL) :-
    last_marker(C, M),
    set(X, Y, M), flipPlayer(C, C2), V1 is V - 1, INVMINMAX is -1 * MINMAX,
    bestChange(C2, V1, IAL, T, INVMINMAX, ALPHA, BETA, _), TE = [T, M, X, Y], unset(M),
    min_or_max(MINMAX, [VAL, T], NEWVAL), alphaBeta(MINMAX, NEWVAL, ALPHA, BETA, IAL, R, V, RE).
change([[C, M, X, Y]|R], V, IAL, [TE|RE], MINMAX, ALPHA, BETA, VAL) :-
    not(last_marker(C, M)),
    position(FROMX, FROMY, M), move(X, Y, M), flipPlayer(C, C2), V1 is V - 1, INVMINMAX is -1 * MINMAX,
    bestChange(C2, V1, IAL, T, INVMINMAX, ALPHA, BETA, _), TE = [T, M, X, Y], move(FROMX, FROMY, M),
    min_or_max(MINMAX, [VAL, T], NEWVAL), alphaBeta(MINMAX, NEWVAL, ALPHA, BETA, IAL, R, V, RE).
change([], _, _, [], _, _, _, _).

%End of IA algorithm, check if an alpha or a Beta cut is possible
alphaBeta(-1, VAL, ALPHA, _, _, _, _, []) :- ALPHA >= VAL.
alphaBeta(-1, VAL, ALPHA, BETA, IAL, R, V, RE) :- min_list([BETA, VAL], BETA2), change(R, V, IAL, RE, -1, ALPHA, BETA2, VAL).
alphaBeta(1, VAL, _, BETA, _, _, _, []) :- VAL >= BETA.
alphaBeta(1, VAL, ALPHA, BETA, IAL, R, V, RE) :- max_list([ALPHA, VAL], ALPHA2), change(R, V, IAL, RE, 1, ALPHA2, BETA, VAL).

%fonction d'evaluation
eval(_,C,100):- winner(C),!.
eval(D,C,-100):- D > 1, flipPlayer(C,C1), winner(C1),!.  %uniquement si le joueur n'a pas choisi le mode facile

%evaluation en mode difficile
eval(3,C,N):- nbSetMarkers(C, NbMC, LMC), NbMC<4, flipPlayer(C,C1),nbSetMarkers(C1, NbMC1, LMC1), NbMC1<4, !, calcNbWinSet(C,NWS, NbMC, LMC), calcNbWinSet(C1,NWS1, NbMC1, LMC1), N is (NbMC*NWS - 1.5*NbMC1*NWS1).
eval(3,C,N):- nbSetMarkers(C, 4, _), flipPlayer(C,C1),nbSetMarkers(C1, 4, _), !,calcAlignment(C,NA), calcAlignment(C1,NA2), N is (4*NA - 7*NA2).
eval(3,C,N):- calcAlignment(C,NA), flipPlayer(C,C1), calcAlignment(C1,NA2), nbSetMarkers(C, NbMC, LMC),nbSetMarkers(C1, NbMC1, LMC1),calcNbWinSet(C,NWS, NbMC, LMC), calcNbWinSet(C1,NWS1, NbMC1, LMC1), N is (4*NA - 7*NA2 + NbMC*NWS - NbMC1*NWS1).

%Calcule le nombre de pions alignes pour une couleur donnee (et verifie si les cases necessitant des pions pour gagner ne sont pas occupees par les pions de l'adversaire).
calcAlignment(C,N):- positions(C, _, _, X2, Y2, X3, Y3, X4, Y4), winner(X, Y, X2, Y2, X3, Y3, X4, Y4), inRange([X,Y], 1, 5), checkEmptyCase(X,Y,EC),!, N is 3*EC.
calcAlignment(C,N):- positions(C, X1, Y1, _, _, X3, Y3, X4, Y4), winner(X1, Y1, X, Y, X3, Y3, X4, Y4), inRange([X,Y], 1, 5), checkEmptyCase(X,Y,EC),!, N is 3*EC.
calcAlignment(C,N):- positions(C, X1, Y1, X2, Y2, _, _, X4, Y4), winner(X1, Y1, X2, Y2, X, Y, X4, Y4), inRange([X,Y], 1, 5), checkEmptyCase(X,Y,EC),!, N is 3*EC.
calcAlignment(C,N):- positions(C, X1, Y1, X2, Y2, X3, Y3, _, _), winner(X1, Y1, X2, Y2, X3, Y3, X, Y), inRange([X,Y], 1, 5), checkEmptyCase(X,Y,EC),!, N is 3*EC.
calcAlignment(_,0).

%Verifie que la case ayant les coordonnee (X,Y) est vide.
checkEmptyCase(X,Y,1):- not(position(X,Y,_)),!.
checkEmptyCase(_,_,0.2).

%calcule le nombre de positions gagnantes possible avec les pions quil reste a placer
calcNbWinSet(C,T, N, L):- N<4, flipPlayer(C1,C), calcPositions(4,L,[X1, Y1, X2, Y2, X3, Y3, X4, Y4]), setof([[X1, Y1], [X2, Y2], [X3, Y3], [X4, Y4]], (winner(X1, Y1, X2, Y2, X3, Y3, X4, Y4), inRange([X1, Y1, X2, Y2, X3, Y3, X4, Y4], 1, 5), noOpponentMarker(C1,[X1, Y1, X2, Y2, X3, Y3, X4, Y4])), L2),deleteDouble(L2,L3), length(L3, T),!.
calcNbWinSet(_,0,_,_).

%calcule le nombre de pions places pour une couleur donnee. Retourne egalement la liste des positions correspondantes aux pions.
nbSetMarkers(C,N, L2):- findall([X,Y], (markers(C, L), member(P,L), position(X,Y,P)),L2), length(L2, N).

%forme une liste contenant les positions de N pions, en utilisants les coordonnees des pions passes en parametre. Utilise ensuite des variables anonymes si le nombre de pions passes en parametre est inferieur a N.
calcPositions(0, _, []):-!.
calcPositions(N, [], [_,_|R]):- N1 is N-1, calcPositions(N1, [], R),!.
calcPositions(N, [[X,Y]|R], [X,Y|R2]):-  N1 is N-1, calcPositions(N1, R, R2).

%Verifie que les positions contenues dans la liste ne sont pas occupees par les pions du joueur C
noOpponentMarker(_,[]):- !.
noOpponentMarker(C,[X,Y|R]):- markers(C,[C1,C2,C3,C4]),not(position(X,Y,C1)),not(position(X,Y,C2)),not(position(X,Y,C3)),not(position(X,Y,C4)), noOpponentMarker(C,R).

%supprime les doublons (deux placements sont consideres comme identiques si le seul changement est la permutation des pions entre eux)
deleteDouble([],[]):-!.
deleteDouble([T|R],R2):- member(T2,R), calcDouble(T,T2), deleteDouble(R,R2),!.
deleteDouble([T|R],[T|R2]):-deleteDouble(R,R2).

%verifie si deux placements sont identiques
calcDouble([],_):-!.
calcDouble([T|R],L):-member(T,L),calcDouble(R,L).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%        ROZGRYWKA        %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

:- dynamic playerType/2.
:- dynamic playerName/2.
:- dynamic firstPlayer/1.
:- dynamic aiLevel/1.

menu:-
        assert(playerType('B', 'czlowiek')), 
		assert(playerType('R', 'AI')), !,
        assert(playerName('B', 'czlowiek')),
        assert(playerName('R', 'AI')),
        assert(firstPlayer('B')),
        assert(aiLevel(3)).
		
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%                ROZSTAWIENIE PIONKOW                %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

placementPos(Pos, Player):-
        last_marker(Player, Marker), readCoordPos(Pos, Player, Marker), !.

placementPosAI(PosX, PosY, Player):-
        aiLevel(L), 
		bestChange(Player, 3, L, M, X, Y), 
		set(PosX, PosY, X, Y, M), !.
		
readCoordPos(Pos, Player, Marker):-
        last_marker(Player, Marker),
        name(Pos, CoordChr), getCoord(Marker, CoordChr, X, Y),
        not(position(X, Y, _)), !, set(X, Y, Marker).
		
getCoord(_, [], _, _).

getCoord(_, [X, Y], X1, Y1):-
        X1 is X - 48, Y1 is Y - 48, 
        inRange([X1, Y1], 1, 5), !.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%             ROZGRYWKA (PRZESTAWIANIE PIONKOW)             %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
getMoveHuman(Move, Player):-
        name(Move, MoveRequestList), consultList(Player, MoveRequestList), !.

getMoveAI(PosX, PosY, PosXO, PosYO):-
        aiLevel(L), bestChange('R', 3, L, Marker, X, Y), move(PosX, PosY, PosXO, PosYO, X, Y, Marker), !.

consultList(Player, [_, AsciiNumber, SimpleMoveAscii]):-
        MarkerNumber is AsciiNumber - 48,
        between(1, 4, MarkerNumber), !,
        concat(Player, MarkerNumber, Marker),
        name(SimpleMove, [SimpleMoveAscii]),
        simpleMove(Marker, SimpleMove).

consultList(Player, [_, AsciiNumber, FirstMoveAscii, SecondMoveAscii]):-
        MarkerNumber is AsciiNumber - 48,
        between(1, 4, MarkerNumber), !,
        concat(Player, MarkerNumber, Marker),
        name(FirstMove, [FirstMoveAscii]),
        name(SecondMove, [SecondMoveAscii]),
        combineMove(Marker, FirstMove, SecondMove).

simpleMove(Marker, SimpleMove):-
        position(X, Y, Marker),
        possibleMove(X, Y, SimpleMove, NewX, NewY), not(position(NewX, NewY, _)), !,
        move(NewX, NewY, Marker).

combineMove(Marker, FirstMove, SecondMove):-
        position(X, Y, Marker),
        possibleMove(X, Y, FirstMove, NewX1, NewY1),
        possibleMove(NewX1, NewY1, SecondMove, NewX2, NewY2),
        not(position(NewX2, NewY2, _)), !,
        move(NewX2, NewY2, Marker).

possibleMove(X, Y, 'g', X, NewY):- NewY is Y - 1, inRange([NewY], 1, 5), !.
possibleMove(X, Y, 'd', X, NewY):- NewY is Y + 1, inRange([NewY], 1, 5), !.
possibleMove(X, Y, 'p', NewX, Y):- NewX is X + 1, inRange([NewX], 1, 5), !.
possibleMove(X, Y, 'l', NewX, Y):- NewX is X - 1, inRange([NewX], 1, 5), !.

:- menu.